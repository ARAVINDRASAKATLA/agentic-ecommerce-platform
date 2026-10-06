package com.DeliveryManagement.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DeliveryManagement.model.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
	List<Driver> findByStatus(String status);
	Optional<Driver> findFirstByStatus(String status);
}
