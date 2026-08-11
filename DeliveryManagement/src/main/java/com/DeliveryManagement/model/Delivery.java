package com.DeliveryManagement.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Delivery {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int deliveryId;
	private int orderId;
	private String trackingNumber;
	private String shippingAddress;
	private LocalDate estimatedDeliveryDate;
	private LocalDate actualDeliveryDate;
	private String status;
	
	
}