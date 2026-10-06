package com.InventoryManagement.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.InventoryManagement.model.inventory;

@Repository
public interface inventoryRepository extends JpaRepository<inventory, Integer> {
	Optional<inventory> findByProductId(int productId);
}
