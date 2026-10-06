package com.DeliveryManagement.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverCreateRequest {
	private String name;
	private String phone;
	private String status; // Default to AVAILABLE if null
}
