package com.Products.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Products.Service.orderService;
import com.Products.dto.Response.userOrderResponse;
import com.Products.dto.request.orderCreateRequest;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class orderController {
	
	@Autowired
	public orderService orderService;
	
	@PostMapping("/create")
	public ResponseEntity<?> createOrder(@RequestBody orderCreateRequest createRequest) {
		try {
			userOrderResponse response = orderService.createOrder(createRequest);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (RuntimeException ex) {
			if (ex.getMessage() != null && ex.getMessage().contains("out of stock")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product is currently out of stock.");
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
	}
	
	@GetMapping("/UserOrder/{id}")
	public ResponseEntity<userOrderResponse> getOrder(@PathVariable("id") int id) {
		return ResponseEntity.ok(orderService.getUserOrders(id));
	}

	@GetMapping("/{id}")
	public ResponseEntity<userOrderResponse> getOrderById(@PathVariable("id") int id) {
		return ResponseEntity.ok(orderService.getUserOrders(id));
	}

	@PutMapping("/{id}/cancel")
	public ResponseEntity<?> cancelOrder(@PathVariable("id") int id) {
		try {
			userOrderResponse response = orderService.cancelOrder(id);
			return ResponseEntity.ok(response);
		} catch (RuntimeException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

}
