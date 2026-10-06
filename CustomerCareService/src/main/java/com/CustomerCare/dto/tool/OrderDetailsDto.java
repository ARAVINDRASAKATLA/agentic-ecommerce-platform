package com.CustomerCare.dto.tool;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDetailsDto {
	private int orderId;
	private int userId;
	private String userName;
	private double totalprice;
	private String status;
	private int deliveryId;
	private String message;
	private List<OrderItemDetailsDto> orderItems;
}
