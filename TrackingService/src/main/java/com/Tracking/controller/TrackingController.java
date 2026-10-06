package com.Tracking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Tracking.dto.LocationUpdateRequest;
import com.Tracking.dto.OrderTrackingResponse;
import com.Tracking.model.TrackingLocation;
import com.Tracking.service.TrackingService;

import org.springframework.web.bind.annotation.CrossOrigin;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tracking")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TrackingController {

	private final TrackingService trackingService;

	@PostMapping("/location")
	public ResponseEntity<TrackingLocation> updateLocation(@RequestBody LocationUpdateRequest request) {
		return ResponseEntity.ok(trackingService.updateDriverLocation(request));
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<OrderTrackingResponse> trackOrder(@PathVariable("orderId") int orderId) {
		return ResponseEntity.ok(trackingService.trackOrder(orderId));
	}

}
