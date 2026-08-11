package com.Products.dto.Response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class orderItemsResponse {
	
     private int orderItemId;
     private int orderId;
	private int productId;
	private int quantity;
	private double price;

}
