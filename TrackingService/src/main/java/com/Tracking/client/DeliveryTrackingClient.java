package com.Tracking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.Tracking.dto.DeliveryInfoDto;
import com.Tracking.dto.DriverInfoDto;

@FeignClient(name = "DeliveryManagement")
public interface DeliveryTrackingClient {

	@GetMapping("/delivery/order/{orderId}")
	DeliveryInfoDto getDeliveryByOrderId(@PathVariable("orderId") int orderId);

	@GetMapping("/drivers/{driverId}")
	DriverInfoDto getDriverById(@PathVariable("driverId") int driverId);

}
