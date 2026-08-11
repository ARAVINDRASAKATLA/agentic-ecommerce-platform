package com.Products.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class orderItemUpdateRequest {
	
	private int productId;
	private int quantity;
	private double price;
	
	
}
