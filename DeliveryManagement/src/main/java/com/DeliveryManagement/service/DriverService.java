package com.DeliveryManagement.service;

import java.util.List;

import com.DeliveryManagement.dtoRequest.DriverCreateRequest;
import com.DeliveryManagement.dtoRequest.VehicleCreateRequest;
import com.DeliveryManagement.dtoResponse.DriverResponse;
import com.DeliveryManagement.model.Vehicle;

public interface DriverService {
	DriverResponse createDriver(DriverCreateRequest request);
	DriverResponse getDriverById(int driverId);
	List<DriverResponse> getAvailableDrivers();
	DriverResponse updateDriverStatus(int driverId, String status);
	Vehicle registerVehicle(VehicleCreateRequest request);
}
