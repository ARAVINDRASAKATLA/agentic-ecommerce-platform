package com.DeliveryManagement.Dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DeliveryManagement.model.Delivery;

@Repository
public interface deliveryRepository extends JpaRepository<Delivery, Integer> {
	Optional<Delivery> findByOrderId(int orderId);
	Optional<Delivery> findByTrackingNumber(String trackingNumber);
}
