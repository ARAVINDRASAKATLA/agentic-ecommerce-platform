package com.CustomerCare.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.CustomerCare.dto.ChatMessageRequest;
import com.CustomerCare.dto.ChatMessageResponse;
import com.CustomerCare.dto.gemini.Candidate;
import com.CustomerCare.dto.gemini.Content;
import com.CustomerCare.dto.gemini.FunctionCall;
import com.CustomerCare.dto.gemini.FunctionResponse;
import com.CustomerCare.dto.gemini.GeminiRequest;
import com.CustomerCare.dto.gemini.GeminiResponse;
import com.CustomerCare.dto.gemini.Part;
import com.CustomerCare.dto.gemini.SystemInstruction;
import com.CustomerCare.dto.gemini.Tool;
import com.CustomerCare.model.ChatMessage;

@Service
public class CustomerCareAiService {

	private static final Logger log = LoggerFactory.getLogger(CustomerCareAiService.class);

	private static final String SYSTEM_PROMPT = """
			You are 'ApexCare AI', a polite, empathetic, and knowledgeable customer service assistant for our E-Commerce platform.
			Your purpose is to assist shoppers with order tracking, product recommendations, delivery updates, returns, stock checks, and cancellations.

			Platform Policies:
			- Shipping: Standard shipping delivers in 2-4 business days. Express shipping delivers in 1-2 business days.
			- Returns: 30-day money-back guarantee on unopened items in original packaging.
			- Cancellations: Allowed immediately if the order has not been dispatched ('OUT_FOR_DELIVERY'). Requires explicit confirmation from customer.
			- Refunds: Automatically credited within 3-5 business days upon item receipt.
			- Driver Tracking: Live GPS coordinates are available once the order is dispatched.

			Tool Usage Instructions:
			- You have access to internal tools: 'lookupOrder', 'trackOrder', 'searchProducts', 'checkProductStock', and 'cancelOrder'.
			- When the user asks about an order or product, call the relevant tool to get real data rather than making assumptions.
			- For order cancellation: If the customer asks to cancel an order without explicit confirmation, invoke cancelOrder with confirmed=false or ask the customer to confirm first. Never execute destructive operations without customer approval.
			- Synthesize the tool output into a clear, helpful, customer-friendly summary.
			""";

	@Value("${gemini.api-key}")
	private String apiKey;

	@Value("${gemini.model:gemini-flash-latest}")
	private String model;

	@Value("${gemini.base-url:https://generativelanguage.googleapis.com/v1beta/models}")
	private String baseUrl;

	@Autowired
	private RestClient restClient;

	@Autowired
	private ChatMemoryService chatMemoryService;

	@Autowired
	private AgentToolService agentToolService;

	public ChatMessageResponse processChat(ChatMessageRequest request, Integer headerUserId, String headerUsername) {
		String sessionId = (request.getSessionId() != null && !request.getSessionId().isBlank())
				? request.getSessionId()
				: UUID.randomUUID().toString();

		String customerIdentity = buildCustomerIdentity(request, headerUserId, headerUsername);
		String userPrompt = request.getMessage();

		log.info("Processing chat for session={}, user={}", sessionId, customerIdentity);

		// 1. Fetch conversation history
		List<ChatMessage> history = chatMemoryService.getHistory(sessionId);

		// 2. Build Gemini request contents
		List<Content> contents = new ArrayList<>();
		for (ChatMessage turn : history) {
			contents.add(Content.builder()
					.role(turn.getRole())
					.parts(List.of(Part.builder().text(turn.getContent()).build()))
					.build());
		}

		// 3. Append current user message (enriched with identity if present)
		String enrichedUserPrompt = userPrompt;
		if (history.isEmpty() && !customerIdentity.isEmpty()) {
			enrichedUserPrompt = String.format("[Customer Info: %s]\n%s", customerIdentity, userPrompt);
		}

		contents.add(Content.builder()
				.role("user")
				.parts(List.of(Part.builder().text(enrichedUserPrompt).build()))
				.build());

		// 4. Multi-step Agent Tool Loop
		List<Tool> tools = agentToolService.getDeclaredTools();
		String aiReply = "";
		int maxSteps = 5;
		int step = 0;

		while (step < maxSteps) {
			step++;
			GeminiRequest geminiRequest = GeminiRequest.builder()
					.systemInstruction(SystemInstruction.builder()
							.parts(List.of(Part.builder().text(SYSTEM_PROMPT).build()))
							.build())
					.contents(contents)
					.tools(tools)
					.build();

			GeminiResponse response;
			try {
				String uri = String.format("%s/%s:generateContent?key=%s", baseUrl, model, apiKey);
				response = restClient.post()
						.uri(uri)
						.contentType(MediaType.APPLICATION_JSON)
						.body(geminiRequest)
						.retrieve()
						.body(GeminiResponse.class);
			} catch (Exception ex) {
				log.error("Failed to query Gemini API on step {}: {}", step, ex.getMessage());
				aiReply = "I apologize, but I am having temporary trouble reaching our customer care systems. Please feel free to rephrase or try again in a moment.";
				break;
			}

			if (response == null || response.getCandidates() == null || response.getCandidates().isEmpty()) {
				aiReply = "I am sorry, but I did not receive a response from the service. How else may I assist you?";
				break;
			}

			Candidate candidate = response.getCandidates().get(0);
			FunctionCall functionCall = candidate.getFunctionCall();

			if (functionCall != null) {
				log.info("Gemini invoked tool: name={}, args={}", functionCall.getName(), functionCall.getArgs());

				// Append model's tool call turn to conversation context
				contents.add(Content.builder()
						.role("model")
						.parts(List.of(Part.builder().functionCall(functionCall).build()))
						.build());

				// Execute the tool
				Map<String, Object> toolResult = agentToolService.executeTool(
						functionCall.getName(),
						functionCall.getArgs() != null ? functionCall.getArgs() : Map.of()
				);

				// Append functionResponse turn
				contents.add(Content.builder()
						.role("user")
						.parts(List.of(Part.builder()
								.functionResponse(FunctionResponse.builder()
										.name(functionCall.getName())
										.response(toolResult)
										.build())
								.build()))
						.build());
				// Loop again so Gemini can synthesize response or call another tool
			} else {
				// Final textual answer
				aiReply = candidate.getText();
				if (aiReply.isBlank()) {
					aiReply = extractTextFromResponse(response);
				}
				break;
			}
		}

		// 5. Record turns in chat memory
		chatMemoryService.addMessage(sessionId, "user", userPrompt);
		chatMemoryService.addMessage(sessionId, "model", aiReply);

		// 6. Generate contextual suggested actions
		List<String> suggestedActions = deriveSuggestedActions(userPrompt, aiReply);

		return ChatMessageResponse.builder()
				.sessionId(sessionId)
				.reply(aiReply)
				.timestamp(LocalDateTime.now())
				.suggestedActions(suggestedActions)
				.build();
	}

	public boolean testConnectivity() {
		try {
			String uri = String.format("%s/%s:generateContent?key=%s", baseUrl, model, apiKey);
			GeminiRequest testRequest = GeminiRequest.builder()
					.contents(List.of(Content.builder()
							.role("user")
							.parts(List.of(Part.builder().text("ping").build()))
							.build()))
					.build();

			GeminiResponse response = restClient.post()
					.uri(uri)
					.contentType(MediaType.APPLICATION_JSON)
					.body(testRequest)
					.retrieve()
					.body(GeminiResponse.class);

			return response != null && response.getCandidates() != null && !response.getCandidates().isEmpty();
		} catch (Exception e) {
			log.error("Gemini connectivity test failed: {}", e.getMessage());
			return false;
		}
	}

	private String buildCustomerIdentity(ChatMessageRequest request, Integer headerUserId, String headerUsername) {
		StringBuilder sb = new StringBuilder();
		if (headerUsername != null && !headerUsername.isBlank()) {
			sb.append("Username: ").append(headerUsername);
		} else if (request.getCustomerName() != null && !request.getCustomerName().isBlank()) {
			sb.append("Name: ").append(request.getCustomerName());
		}

		Integer resolvedId = headerUserId != null ? headerUserId : request.getUserId();
		if (resolvedId != null) {
			if (sb.length() > 0) sb.append(", ");
			sb.append("User ID: ").append(resolvedId);
		}
		return sb.toString();
	}

	private String extractTextFromResponse(GeminiResponse response) {
		if (response == null || response.getCandidates() == null || response.getCandidates().isEmpty()) {
			return "Hello! I am here to help you with your shopping experience. What can I assist you with today?";
		}
		Candidate candidate = response.getCandidates().get(0);
		if (candidate.getContent() == null || candidate.getContent().getParts() == null) {
			return "No message content was returned.";
		}
		StringBuilder sb = new StringBuilder();
		for (Part part : candidate.getContent().getParts()) {
			if (part.getText() != null) {
				sb.append(part.getText());
			}
		}
		return sb.toString().trim();
	}

	private List<String> deriveSuggestedActions(String userPrompt, String aiReply) {
		String combined = (userPrompt + " " + aiReply).toLowerCase();
		List<String> suggestions = new ArrayList<>();

		if (combined.contains("track") || combined.contains("order") || combined.contains("delivery")) {
			suggestions.add("Track My Order");
			suggestions.add("Check Delivery Status");
		}
		if (combined.contains("return") || combined.contains("refund") || combined.contains("cancel")) {
			suggestions.add("View Return Policy");
			suggestions.add("Initiate Return");
		}
		if (combined.contains("product") || combined.contains("price") || combined.contains("stock")) {
			suggestions.add("Browse Catalog");
			suggestions.add("Check Product Stock");
		}

		if (suggestions.isEmpty()) {
			suggestions.add("Track an Order");
			suggestions.add("Return & Refund Policy");
			suggestions.add("Contact Human Support");
		}
		return suggestions;
	}
}
