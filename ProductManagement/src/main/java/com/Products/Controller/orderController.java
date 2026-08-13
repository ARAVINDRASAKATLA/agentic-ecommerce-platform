package com.Products.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Products.Service.orderService;
import com.Products.dto.Response.userOrderResponse;
import com.Products.dto.request.orderCreateRequest;

@RestController
@RequestMapping("/orders")
public class orderController {
	
	@Autowired
	public orderService orderService;
	
	@PostMapping("/create")
	public userOrderResponse createOrder(@RequestBody orderCreateRequest createRequest) {
		  return orderService.createOrder(createRequest);
		
	}
	
	
	@GetMapping("UserOrder/{id}")
	 public userOrderResponse getOrder(@PathVariable("id") int id) {
		 return orderService.getUserOrders(id);
	 }
	
	

}
