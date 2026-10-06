package com.DeliveryManagement.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleCreateRequest {
	private String vehicleNumber;
	private String vehicleType; // BIKE, CAR, VAN
	private Integer driverId;
}
