package com.Products.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.Products.dto.event.OrderEvent;

@Service
public class OrderEventProducer {

	private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);
	private static final String TOPIC = "order-events";

	@Autowired(required = false)
	private KafkaTemplate<String, OrderEvent> kafkaTemplate;

	public void publishOrderEvent(OrderEvent event) {
		if (kafkaTemplate == null) {
			log.info("KafkaTemplate not configured; skipping event publish for order {}", event.getOrderId());
			return;
		}

		try {
			kafkaTemplate.send(TOPIC, String.valueOf(event.getOrderId()), event)
					.whenComplete((result, ex) -> {
						if (ex != null) {
							log.warn("Could not publish OrderEvent for order {}: {}", event.getOrderId(), ex.getMessage());
						} else {
							log.info("OrderEvent published to {} for order {} [offset={}]",
									TOPIC, event.getOrderId(), result.getRecordMetadata().offset());
						}
					});
		} catch (Exception ex) {
			log.warn("Kafka non-blocking send caught: {}", ex.getMessage());
		}
	}
}
