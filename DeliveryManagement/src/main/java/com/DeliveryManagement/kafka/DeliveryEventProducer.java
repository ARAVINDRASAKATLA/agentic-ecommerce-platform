package com.DeliveryManagement.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.DeliveryManagement.dto.event.DeliveryEvent;

@Service
public class DeliveryEventProducer {

	private static final Logger log = LoggerFactory.getLogger(DeliveryEventProducer.class);
	private static final String TOPIC = "delivery-events";

	@Autowired(required = false)
	private KafkaTemplate<String, DeliveryEvent> kafkaTemplate;

	public void publishDeliveryEvent(DeliveryEvent event) {
		if (kafkaTemplate == null) {
			log.info("KafkaTemplate not configured; skipping event publish for delivery {}", event.getDeliveryId());
			return;
		}

		try {
			kafkaTemplate.send(TOPIC, String.valueOf(event.getDeliveryId()), event)
					.whenComplete((result, ex) -> {
						if (ex != null) {
							log.warn("Could not publish DeliveryEvent for delivery {}: {}", event.getDeliveryId(), ex.getMessage());
						} else {
							log.info("DeliveryEvent published to {} for delivery {} [status={}]",
									TOPIC, event.getDeliveryId(), event.getStatus());
						}
					});
		} catch (Exception ex) {
			log.warn("Kafka non-blocking send caught: {}", ex.getMessage());
		}
	}
}
