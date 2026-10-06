package com.CustomerCare.kafka;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.CustomerCare.dto.event.PlatformEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CareEventConsumer {

	private static final Logger log = LoggerFactory.getLogger(CareEventConsumer.class);
	private static final int MAX_BUFFER_SIZE = 100;

	private final Deque<PlatformEvent> recentEvents = new ConcurrentLinkedDeque<>();

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topics = "order-events", groupId = "customer-care-service-group")
	public void consumeOrderEvent(String message) {
		log.info("CareEventConsumer received order-event: {}", message);
		processAndStoreEvent("order-events", message);
	}

	@KafkaListener(topics = "delivery-events", groupId = "customer-care-service-group")
	public void consumeDeliveryEvent(String message) {
		log.info("CareEventConsumer received delivery-event: {}", message);
		processAndStoreEvent("delivery-events", message);
	}

	@KafkaListener(topics = "tracking-events", groupId = "customer-care-service-group")
	public void consumeTrackingEvent(String message) {
		log.info("CareEventConsumer received tracking-event: {}", message);
		processAndStoreEvent("tracking-events", message);
	}

	public void processAndStoreEvent(String topic, String rawJson) {
		try {
			JsonNode node = objectMapper.readTree(rawJson);
			String eventId = node.has("eventId") ? node.get("eventId").asText() : null;
			String eventType = node.has("eventType") ? node.get("eventType").asText() : "UNKNOWN";
			Integer orderId = node.has("orderId") && !node.get("orderId").isNull() ? node.get("orderId").asInt() : null;
			Integer deliveryId = node.has("deliveryId") && !node.get("deliveryId").isNull() ? node.get("deliveryId").asInt() : null;
			Integer driverId = node.has("driverId") && !node.get("driverId").isNull() ? node.get("driverId").asInt() : null;

			PlatformEvent event = PlatformEvent.builder()
					.eventId(eventId)
					.eventType(eventType)
					.topic(topic)
					.orderId(orderId)
					.deliveryId(deliveryId)
					.driverId(driverId)
					.payload(rawJson)
					.receivedAt(LocalDateTime.now())
					.build();

			storeEvent(event);
		} catch (Exception e) {
			log.warn("Failed to parse Kafka event payload: {}. Storing as raw event.", e.getMessage());
			PlatformEvent rawEvent = PlatformEvent.builder()
					.eventType("RAW")
					.topic(topic)
					.payload(rawJson)
					.receivedAt(LocalDateTime.now())
					.build();
			storeEvent(rawEvent);
		}
	}

	public void storeEvent(PlatformEvent event) {
		recentEvents.addFirst(event);
		while (recentEvents.size() > MAX_BUFFER_SIZE) {
			recentEvents.pollLast();
		}
	}

	public List<PlatformEvent> getRecentEvents(Integer orderId, Integer limit) {
		int max = (limit == null || limit <= 0) ? 50 : Math.min(limit, MAX_BUFFER_SIZE);
		return recentEvents.stream()
				.filter(e -> orderId == null || (e.getOrderId() != null && e.getOrderId().equals(orderId)))
				.limit(max)
				.collect(Collectors.toList());
	}

	public void clearEvents() {
		recentEvents.clear();
	}

	public int getEventCount() {
		return recentEvents.size();
	}
}
