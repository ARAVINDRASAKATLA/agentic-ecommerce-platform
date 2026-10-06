package com.Tracking.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTrackingResponse {
	private int orderId;
	private int deliveryId;
	private Integer driverId;
	private String driverName;
	private String driverPhone;
	private String vehicleNumber;
	private String vehicleType;
	private Double latitude;
	private Double longitude;
	private LocalDateTime lastUpdated;
	private String deliveryStatus;
	private String pickupLocation;
	private String deliveryLocation;
	private String message;
}
