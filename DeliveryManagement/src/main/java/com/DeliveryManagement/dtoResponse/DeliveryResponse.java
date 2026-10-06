package com.DeliveryManagement.dtoResponse;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponse {
	
	private int deliveryId;
	private int orderId;
	private Integer customerId;
	private Integer driverId;
	private Integer vehicleId;
	private String trackingNumber;
	private String pickupLocation;
	private String deliveryLocation;
	private String shippingAddress;
	private LocalDate estimatedDeliveryDate;
	private LocalDate actualDeliveryDate;
	private String status;

	public String getShippingAddress() {
		return (shippingAddress != null && !shippingAddress.isEmpty()) ? shippingAddress : deliveryLocation;
	}
}
