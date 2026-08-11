package com.Products.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.Products.Model.orderItems;
import com.Products.Model.userOrder;
import com.Products.dto.Response.orderItemsResponse;
import com.Products.dto.Response.userOrderResponse;
import com.Products.dto.Response.userResponse;
import com.Products.dto.request.orderCreateRequest;
import com.Products.dto.request.orderItemCreatRequest;

public class orderBuilder {
	
	@Autowired
	RestTemplate restTemplate ;
	
	public static userOrder buildOrderCreateRequest(orderCreateRequest orderRequest) {
		
		   return  userOrder.builder()
				   .userId(orderRequest.getUserId())
		     .status(orderRequest.getStatus())
		    
		     .orderItems(buildOrderItemsRequest(orderRequest.getOrdetItems()))
		     .build();		   
	}
	
	public static orderItems buildOrderItemRequest(orderItemCreatRequest orderItemsRequest) {
		  
		  return orderItems.builder()
		  .productId(orderItemsRequest.getProductId())
		  .quantity(orderItemsRequest.getQuantity()).build();
		  
		  
	}
	
	public static List<orderItems> buildOrderItemsRequest(List<orderItemCreatRequest> orderItemRequest) {
	    return new ArrayList<>(
	            orderItemRequest.stream()
	                    .map(orderBuilder::buildOrderItemRequest)
	                    .collect(Collectors.toList())
	    );
	}
	
	public static userOrderResponse builduserOrderResponse(userOrder order, userResponse user) {
		return userOrderResponse.builder()
				.userId(order.getUserId())
		.orderId(order.getOrderId())
		.userName(user.getUserName())
		.totalprice(order.getTotalprice())
		.status(order.getStatus())
		.orderTime(order.getOrderTime())
		.orderItems(buildOrderItemsResponse(order.getOrderItems())).build();
	}
	
	public static List<orderItemsResponse> buildOrderItemsResponse(List<orderItems> items) {

	    return items.stream()
	            .map(orderBuilder::buildOrderItemResponse)
	            .toList();
	}
	
	public static orderItemsResponse buildOrderItemResponse(orderItems item) {

	    return orderItemsResponse.builder()
	            .orderItemId(item.getOrderItemId())
	            .productId(item.getProductId())
	            .quantity(item.getQuantity())
	            .price(item.getPrice())
	            .build();
	}
	


}
