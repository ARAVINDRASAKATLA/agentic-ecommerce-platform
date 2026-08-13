package com.DeliveryManagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DeliveryManagement.dtoRequest.DeliveryCreateRequest;
import com.DeliveryManagement.dtoRequest.updateDeliveryRequest;
import com.DeliveryManagement.dtoResponse.DeliveryResponse;
import com.DeliveryManagement.serviceImpl.deliveryService;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {
	
	@Autowired
	deliveryService deliveryService;
	
	@PostMapping("/deliveries")
	public DeliveryResponse deliveries(@RequestBody DeliveryCreateRequest createRequest) {
		return deliveryService.deliveries(createRequest);
	}

	
	@PutMapping("/updateDelivery")
	public DeliveryResponse updateDelivery(@RequestBody updateDeliveryRequest deliveryRequest) {
		           
		return deliveryService.updateDelivery(deliveryRequest);
	}
}
