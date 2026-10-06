package com.InventoryManagement.service;

import com.InventoryManagement.dto.InventoryCheckRequest;
import com.InventoryManagement.dto.InventoryReleaseRequest;
import com.InventoryManagement.dto.InventoryReserveRequest;
import com.InventoryManagement.dto.InventoryResponse;

public interface InventoryService {
	InventoryResponse getInventoryByProductId(int productId);
	InventoryResponse checkStock(InventoryCheckRequest checkRequest);
	InventoryResponse reserveStock(InventoryReserveRequest reserveRequest);
	InventoryResponse releaseStock(InventoryReleaseRequest releaseRequest);
	InventoryResponse addOrUpdateStock(int productId, int availableQuantity, String warehouse);
}
