package com.DeliveryManagement.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.DeliveryManagement.dtoRequest.DriverCreateRequest;
import com.DeliveryManagement.dtoRequest.VehicleCreateRequest;
import com.DeliveryManagement.dtoResponse.DriverResponse;
import com.DeliveryManagement.model.Vehicle;
import com.DeliveryManagement.service.DriverService;

import org.springframework.web.bind.annotation.CrossOrigin;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/drivers")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DriverController {

	private final DriverService driverService;

	@PostMapping
	public ResponseEntity<DriverResponse> createDriver(@RequestBody DriverCreateRequest request) {
		return ResponseEntity.ok(driverService.createDriver(request));
	}

	@GetMapping("/available")
	public ResponseEntity<List<DriverResponse>> getAvailableDrivers() {
		return ResponseEntity.ok(driverService.getAvailableDrivers());
	}

	@GetMapping("/{driverId}")
	public ResponseEntity<DriverResponse> getDriverById(@PathVariable("driverId") int driverId) {
		return ResponseEntity.ok(driverService.getDriverById(driverId));
	}

	@PutMapping("/{driverId}/status")
	public ResponseEntity<DriverResponse> updateDriverStatus(@PathVariable("driverId") int driverId,
															 @RequestParam("status") String status) {
		return ResponseEntity.ok(driverService.updateDriverStatus(driverId, status));
	}

	@PostMapping("/vehicle")
	public ResponseEntity<Vehicle> registerVehicle(@RequestBody VehicleCreateRequest request) {
		return ResponseEntity.ok(driverService.registerVehicle(request));
	}

}
