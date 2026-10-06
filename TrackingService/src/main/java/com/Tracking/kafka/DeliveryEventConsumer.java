package com.Tracking.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.Tracking.dto.event.DeliveryEvent;

@Service
public class DeliveryEventConsumer {

	private static final Logger log = LoggerFactory.getLogger(DeliveryEventConsumer.class);

	@KafkaListener(topics = "delivery-events", groupId = "tracking-service-group")
	public void handleDeliveryEvent(DeliveryEvent event) {
		log.info("TrackingService received DeliveryEvent: deliveryId={}, orderId={}, status={}",
				event.getDeliveryId(), event.getOrderId(), event.getStatus());

		if ("OUT_FOR_DELIVERY".equalsIgnoreCase(event.getStatus()) || "DRIVER_ASSIGNED".equalsIgnoreCase(event.getStatus())) {
			log.info("TrackingService initialized tracking stream for order #{}", event.getOrderId());
		}
	}
}
