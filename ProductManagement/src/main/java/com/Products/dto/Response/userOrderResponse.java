package com.Products.dto.Response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class userOrderResponse {
	
	private int userId;
	private int orderId;
	private String userName;
	private double totalprice;
	private String status;
	private LocalDateTime orderTime;
	private Integer deliveryId;
	private String message;
	private List<orderItemsResponse> orderItems;

}
