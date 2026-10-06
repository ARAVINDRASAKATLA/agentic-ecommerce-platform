package com.DeliveryManagement.Dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DeliveryManagement.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
	Optional<Vehicle> findByDriverId(Integer driverId);
	Optional<Vehicle> findByVehicleNumber(String vehicleNumber);
}
