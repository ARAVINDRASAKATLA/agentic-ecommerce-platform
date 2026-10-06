package com.DeliveryManagement.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {
	private int driverId;
	private String name;
	private String phone;
	private String status;
	private String vehicleNumber;
	private String vehicleType;
}
