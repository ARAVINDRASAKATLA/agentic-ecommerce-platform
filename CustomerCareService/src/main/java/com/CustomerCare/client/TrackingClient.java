package com.CustomerCare.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.CustomerCare.dto.tool.TrackingDetailsDto;

@FeignClient(name = "TrackingService", url = "${tracking.service.url:http://localhost:8085}")
public interface TrackingClient {

	@GetMapping("/tracking/order/{orderId}")
	TrackingDetailsDto getTracking(@PathVariable("orderId") Long orderId);
}
