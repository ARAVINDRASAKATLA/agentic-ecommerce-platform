package com.Products.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class orderUpdateRequest {
	
	private int orderId;
	private double totalprice;
	private String status;
	private List<orderUpdateRequest> orderUpdate;
	
}
