package com.Tracking.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.Tracking.dto.event.TrackingEvent;

@Service
public class TrackingEventProducer {

	private static final Logger log = LoggerFactory.getLogger(TrackingEventProducer.class);
	private static final String TOPIC = "tracking-events";

	@Autowired(required = false)
	private KafkaTemplate<String, TrackingEvent> kafkaTemplate;

	public void publishTrackingEvent(TrackingEvent event) {
		if (kafkaTemplate == null) {
			log.info("KafkaTemplate not configured; skipping tracking event for order {}", event.getOrderId());
			return;
		}

		try {
			kafkaTemplate.send(TOPIC, String.valueOf(event.getOrderId()), event)
					.whenComplete((result, ex) -> {
						if (ex != null) {
							log.warn("Could not publish TrackingEvent for order {}: {}", event.getOrderId(), ex.getMessage());
						} else {
							log.info("TrackingEvent published to {} for order {} [lat={}, lng={}]",
									TOPIC, event.getOrderId(), event.getLatitude(), event.getLongitude());
						}
					});
		} catch (Exception ex) {
			log.warn("Kafka non-blocking send caught: {}", ex.getMessage());
		}
	}
}
