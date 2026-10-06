package com.Products.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.Products.dto.Response.DeliveryResponse;
import com.Products.dto.request.DeliveryCreateRequest;

@FeignClient(name = "DeliveryManagement")
public interface DeliveryClient {

	@PostMapping("/delivery/deliveries")
	DeliveryResponse createDelivery(@RequestBody DeliveryCreateRequest createRequest);

}
