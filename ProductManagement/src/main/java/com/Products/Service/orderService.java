package com.Products.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.Products.Builder.orderBuilder;
import com.Products.Dao.orderRepository;
import com.Products.Dao.productRepository;
import com.Products.Model.orderItems;
import com.Products.Model.product;
import com.Products.Model.userOrder;
import com.Products.dto.Response.DeliveryResponse;
import com.Products.dto.Response.userOrderResponse;
import com.Products.dto.Response.userResponse;
import com.Products.dto.request.DeliveryCreateRequest;
import com.Products.dto.request.orderCreateRequest;
import com.Products.serviceImpl.orderServiceImpl;


@Service
public class orderService implements orderServiceImpl{
	
	@Autowired
	orderRepository orderRepository;
	
	@Autowired
	productRepository productRepository;
	
	@Autowired
	RestTemplate restTemplate;
	
	

	@Override
	public userOrderResponse createOrder(orderCreateRequest orderRequest) {
		// TODO Auto-generated method stub
		userOrder order=orderBuilder.buildOrderCreateRequest(orderRequest);
		double totalPrice=0;
		
		for(orderItems items: order.getOrderItems()) {
			  product product = productRepository.findById(items.getProductId()).orElseThrow(()-> new RuntimeException("No Product Found"));
			  items.setPrice(product.getPrice());
			  totalPrice+=product.getPrice()*items.getQuantity();
			  items.setOrder(order);
		}
		
		    order.setTotalprice(totalPrice);
		    order.setOrderTime(LocalDateTime.now());
		  userOrder savedOrder = orderRepository.save(order);
		  
		  String defaultTrackNum=UUID.randomUUID().toString();
		  
		   DeliveryCreateRequest deliveryCreateRequest = DeliveryCreateRequest.builder()
		   .orderId(savedOrder.getOrderId())
		   .trackingNumber(defaultTrackNum)
		   .ShippingAddress("Hyderabad")
		   .estimatedDeliveryDate(LocalDate.now().plusDays(7))
		   .status("PLACED")
		   
		   .build();
		   
		   
		  
		   
		 restTemplate.postForObject("http://localhost:8084/deliveries", deliveryCreateRequest, DeliveryResponse.class);
		  
		//userOrder orderCreateRequest = orderBuilder.buildOrderCreateRequest(orderRequest);
		 //  userOrder save = orderRepository.save(savedOrder);
		  
		  userResponse userResponse = restTemplate.getForObject("http://UserManagement/user/"+order.getUserId(), userResponse.class);
		    return orderBuilder.builduserOrderResponse(savedOrder, userResponse);
	}



	@Override
	public userOrderResponse getUserOrders(int id) {
		// TODO Auto-generated method stub
		  userOrder userOrder = orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order Id Not Found"));
       userResponse userResponse = restTemplate.getForObject("http://UserManagement/user/"+userOrder.getUserId(), userResponse.class );
		
		return orderBuilder.builduserOrderResponse(userOrder, userResponse);
	}

}
