package com.DeliveryManagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DeliveryManagement.dtoRequest.DeliveryCreateRequest;
import com.DeliveryManagement.dtoRequest.updateDeliveryRequest;
import com.DeliveryManagement.dtoResponse.DeliveryResponse;
import com.DeliveryManagement.serviceImpl.deliveryService;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {
	
	@Autowired
	private deliveryService deliveryService;
	
	@PostMapping("/deliveries")
	public ResponseEntity<DeliveryResponse> deliveries(@RequestBody DeliveryCreateRequest createRequest) {
		return ResponseEntity.ok(deliveryService.deliveries(createRequest));
	}

	@PutMapping("/updateDelivery")
	public ResponseEntity<DeliveryResponse> updateDelivery(@RequestBody updateDeliveryRequest deliveryRequest) {
		return ResponseEntity.ok(deliveryService.updateDelivery(deliveryRequest));
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<DeliveryResponse> getDeliveryByOrderId(@PathVariable("orderId") int orderId) {
		return ResponseEntity.ok(deliveryService.getDeliveryByOrderId(orderId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<DeliveryResponse> getDeliveryById(@PathVariable("id") int id) {
		return ResponseEntity.ok(deliveryService.getDeliveryById(id));
	}

	@PutMapping("/{deliveryId}/assign-driver/{driverId}")
	public ResponseEntity<DeliveryResponse> assignDriver(@PathVariable("deliveryId") int deliveryId,
														 @PathVariable("driverId") int driverId) {
		return ResponseEntity.ok(deliveryService.assignDriver(deliveryId, driverId));
	}

	@PutMapping("/{deliveryId}/status")
	public ResponseEntity<DeliveryResponse> updateStatus(@PathVariable("deliveryId") int deliveryId,
														 @RequestParam("status") String status) {
		return ResponseEntity.ok(deliveryService.updateStatus(deliveryId, status));
	}
}
