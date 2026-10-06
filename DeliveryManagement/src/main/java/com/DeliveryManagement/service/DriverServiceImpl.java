package com.DeliveryManagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.DeliveryManagement.Dao.DriverRepository;
import com.DeliveryManagement.Dao.VehicleRepository;
import com.DeliveryManagement.dtoRequest.DriverCreateRequest;
import com.DeliveryManagement.dtoRequest.VehicleCreateRequest;
import com.DeliveryManagement.dtoResponse.DriverResponse;
import com.DeliveryManagement.model.Driver;
import com.DeliveryManagement.model.Vehicle;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

	private final DriverRepository driverRepository;
	private final VehicleRepository vehicleRepository;

	@Override
	public DriverResponse createDriver(DriverCreateRequest request) {
		Driver driver = Driver.builder()
				.name(request.getName())
				.phone(request.getPhone())
				.status(request.getStatus() != null ? request.getStatus().toUpperCase() : "AVAILABLE")
				.build();
		Driver saved = driverRepository.save(driver);
		return mapToResponse(saved);
	}

	@Override
	public DriverResponse getDriverById(int driverId) {
		Driver driver = driverRepository.findById(driverId)
				.orElseThrow(() -> new RuntimeException("Driver not found with id: " + driverId));
		return mapToResponse(driver);
	}

	@Override
	public List<DriverResponse> getAvailableDrivers() {
		return driverRepository.findByStatus("AVAILABLE").stream()
				.map(this::mapToResponse)
				.toList();
	}

	@Override
	public DriverResponse updateDriverStatus(int driverId, String status) {
		Driver driver = driverRepository.findById(driverId)
				.orElseThrow(() -> new RuntimeException("Driver not found with id: " + driverId));
		driver.setStatus(status.toUpperCase());
		Driver saved = driverRepository.save(driver);
		return mapToResponse(saved);
	}

	@Override
	public Vehicle registerVehicle(VehicleCreateRequest request) {
		Vehicle vehicle = Vehicle.builder()
				.vehicleNumber(request.getVehicleNumber())
				.vehicleType(request.getVehicleType() != null ? request.getVehicleType().toUpperCase() : "BIKE")
				.driverId(request.getDriverId())
				.build();
		return vehicleRepository.save(vehicle);
	}

	private DriverResponse mapToResponse(Driver driver) {
		Optional<Vehicle> vehicleOpt = vehicleRepository.findByDriverId(driver.getDriverId());
		return DriverResponse.builder()
				.driverId(driver.getDriverId())
				.name(driver.getName())
				.phone(driver.getPhone())
				.status(driver.getStatus())
				.vehicleNumber(vehicleOpt.map(Vehicle::getVehicleNumber).orElse("Unassigned"))
				.vehicleType(vehicleOpt.map(Vehicle::getVehicleType).orElse("BIKE"))
				.build();
	}

}
