package com.CustomerCare.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CustomerCare.dto.ChatMessageRequest;
import com.CustomerCare.dto.ChatMessageResponse;
import com.CustomerCare.dto.event.PlatformEvent;
import com.CustomerCare.kafka.CareEventConsumer;
import com.CustomerCare.model.ChatMessage;
import com.CustomerCare.service.ChatMemoryService;
import com.CustomerCare.service.CustomerCareAiService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/care")
@CrossOrigin(origins = "*")
public class CustomerCareController {

	@Autowired
	private CustomerCareAiService aiService;

	@Autowired
	private ChatMemoryService chatMemoryService;

	@Autowired(required = false)
	private CareEventConsumer careEventConsumer;

	@GetMapping("/events")
	public ResponseEntity<List<PlatformEvent>> getEvents(
			@RequestParam(required = false) Integer orderId,
			@RequestParam(required = false, defaultValue = "50") Integer limit) {
		if (careEventConsumer == null) {
			return ResponseEntity.ok(List.of());
		}
		return ResponseEntity.ok(careEventConsumer.getRecentEvents(orderId, limit));
	}

	@PostMapping("/chat")
	public ResponseEntity<ChatMessageResponse> chat(
			@RequestBody ChatMessageRequest request,
			@RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
			@RequestHeader(value = "X-Username", required = false) String headerUsername) {

		if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
			return ResponseEntity.badRequest().body(ChatMessageResponse.builder()
					.sessionId(request.getSessionId())
					.reply("Please provide a valid question or message.")
					.build());
		}

		ChatMessageResponse response = aiService.processChat(request, headerUserId, headerUsername);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/history/{sessionId}")
	public ResponseEntity<List<ChatMessage>> getHistory(@PathVariable String sessionId) {
		List<ChatMessage> history = chatMemoryService.getHistory(sessionId);
		return ResponseEntity.ok(history);
	}

	@DeleteMapping("/history/{sessionId}")
	public ResponseEntity<Map<String, String>> clearHistory(@PathVariable String sessionId) {
		chatMemoryService.clearSession(sessionId);
		return ResponseEntity.ok(Map.of("message", "Session conversation history cleared.", "sessionId", sessionId));
	}

	@GetMapping("/health")
	public ResponseEntity<Map<String, Object>> health() {
		boolean geminiConnected = aiService.testConnectivity();
		return ResponseEntity.ok(Map.of(
				"service", "CustomerCareService",
				"status", "UP",
				"geminiConnected", geminiConnected,
				"activeSessions", chatMemoryService.getActiveSessionCount()
		));
	}

	@GetMapping("/faq")
	public ResponseEntity<List<Map<String, String>>> getFaqs() {
		return ResponseEntity.ok(List.of(
				Map.of(
						"question", "How long does shipping take?",
						"answer", "Standard shipping takes 2-4 business days. Express shipping delivers in 1-2 business days."
				),
				Map.of(
						"question", "What is the return policy?",
						"answer", "We offer a 30-day money-back guarantee on unopened items in original packaging."
				),
				Map.of(
						"question", "How can I track my package?",
						"answer", "Once your order is dispatched, you can view real-time GPS tracking using your Order ID."
				),
				Map.of(
						"question", "Can I cancel an order?",
						"answer", "Orders can be cancelled before they reach 'OUT_FOR_DELIVERY' status."
				)
		));
	}
}
