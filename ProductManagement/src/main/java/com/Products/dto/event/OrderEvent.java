package com.Products.dto.event;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent implements Serializable {
	private int orderId;
	private int userId;
	private double totalPrice;
	private String eventType; // "ORDER_CONFIRMED", "ORDER_CANCELLED"
	private String status;
	private int deliveryId;
	private LocalDateTime timestamp;
}
