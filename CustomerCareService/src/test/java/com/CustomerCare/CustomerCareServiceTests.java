package com.CustomerCare;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.CustomerCare.model.ChatMessage;
import com.CustomerCare.service.ChatMemoryService;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerCareServiceTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ChatMemoryService chatMemoryService;

	@BeforeEach
	void setUp() {
		chatMemoryService.clearSession("test-session");
	}

	@Test
	void testChatMemoryService() {
		chatMemoryService.addMessage("test-session", "user", "What is my order status?");
		chatMemoryService.addMessage("test-session", "model", "Please provide your order ID.");

		List<ChatMessage> history = chatMemoryService.getHistory("test-session");
		assertEquals(2, history.size());
		assertEquals("user", history.get(0).getRole());
		assertEquals("model", history.get(1).getRole());

		chatMemoryService.clearSession("test-session");
		assertTrue(chatMemoryService.getHistory("test-session").isEmpty());
	}

	@Test
	void testFaqEndpoint() throws Exception {
		mockMvc.perform(get("/care/faq"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].question").exists())
				.andExpect(jsonPath("$[0].answer").exists());
	}

	@Test
	void testHistoryAndClearEndpoint() throws Exception {
		chatMemoryService.addMessage("session-123", "user", "Hello");

		mockMvc.perform(get("/care/history/session-123"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].content").value("Hello"));

		mockMvc.perform(delete("/care/history/session-123"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Session conversation history cleared."));
	}

	@Test
	void testEmptyChatValidation() throws Exception {
		mockMvc.perform(post("/care/chat")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"sessionId\":\"test-session\",\"message\":\"\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.reply").value("Please provide a valid question or message."));
	}

	@Test
	void testLiveGeminiChat() throws Exception {
		mockMvc.perform(post("/care/chat")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"sessionId\":\"test-live-session\",\"message\":\"Hi, what are your standard shipping timelines?\",\"customerName\":\"Alex\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.reply").isNotEmpty())
				.andExpect(jsonPath("$.sessionId").value("test-live-session"))
				.andExpect(jsonPath("$.suggestedActions").isArray());
	}

	@Autowired
	private com.CustomerCare.service.AgentToolService agentToolService;

	@Test
	void testAgentToolDeclarations() {
		var tools = agentToolService.getDeclaredTools();
		assertNotNull(tools);
		assertEquals(1, tools.size());
		var funcs = tools.get(0).getFunctionDeclarations();
		assertEquals(5, funcs.size()); // lookupOrder, trackOrder, searchProducts, checkProductStock, cancelOrder
	}

	@Test
	void testCancelOrderSafeguardRequiresConfirmation() {
		// When confirmed is false
		var result = agentToolService.executeTool("cancelOrder", java.util.Map.of("orderId", 99, "confirmed", false));
		assertEquals("CONFIRMATION_REQUIRED", result.get("status"));
		assertTrue(result.get("message").toString().contains("requires explicit user confirmation"));
	}

	@Autowired
	private com.CustomerCare.kafka.CareEventConsumer careEventConsumer;

	@Test
	void testKafkaEventProcessingAndEndpoint() throws Exception {
		careEventConsumer.clearEvents();
		String sampleEvent = "{\"eventId\":\"evt-101\",\"eventType\":\"ORDER_CONFIRMED\",\"orderId\":42,\"totalAmount\":99.99}";
		careEventConsumer.processAndStoreEvent("order-events", sampleEvent);

		assertEquals(1, careEventConsumer.getEventCount());
		var events = careEventConsumer.getRecentEvents(42, 10);
		assertEquals(1, events.size());
		assertEquals("ORDER_CONFIRMED", events.get(0).getEventType());

		mockMvc.perform(get("/care/events?orderId=42"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].eventId").value("evt-101"))
				.andExpect(jsonPath("$[0].eventType").value("ORDER_CONFIRMED"))
				.andExpect(jsonPath("$[0].orderId").value(42));
	}
}

