package com.Products.serviceImpl;

import com.Products.dto.Response.userOrderResponse;
import com.Products.dto.request.orderCreateRequest;

public interface orderServiceImpl {
	
	public userOrderResponse createOrder(orderCreateRequest orderRequest);
	
	
	
	public userOrderResponse getUserOrders(int id);

	public userOrderResponse cancelOrder(int id);

}
