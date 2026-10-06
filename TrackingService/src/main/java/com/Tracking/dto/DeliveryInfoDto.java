package com.Tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfoDto {
	private int deliveryId;
	private int orderId;
	private Integer customerId;
	private Integer driverId;
	private Integer vehicleId;
	private String trackingNumber;
	private String pickupLocation;
	private String deliveryLocation;
	private String status;
}
