package com.DeliveryManagement.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.DeliveryManagement.dto.event.OrderEvent;

@Service
public class OrderEventConsumer {

	private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

	@KafkaListener(topics = "order-events", groupId = "delivery-service-group")
	public void handleOrderEvent(OrderEvent event) {
		log.info("DeliveryManagement received OrderEvent: orderId={}, eventType={}, status={}",
				event.getOrderId(), event.getEventType(), event.getStatus());

		if ("ORDER_CONFIRMED".equalsIgnoreCase(event.getEventType())) {
			log.info("Processing async delivery preparation for confirmed order #{}", event.getOrderId());
		} else if ("ORDER_CANCELLED".equalsIgnoreCase(event.getEventType())) {
			log.info("Processing delivery cancellation for cancelled order #{}", event.getOrderId());
		}
	}
}
