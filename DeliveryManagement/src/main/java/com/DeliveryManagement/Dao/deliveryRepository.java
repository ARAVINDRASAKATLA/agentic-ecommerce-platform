package com.DeliveryManagement.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DeliveryManagement.model.Delivery;

@Repository
public interface deliveryRepository extends JpaRepository<Delivery, Integer> {

}
