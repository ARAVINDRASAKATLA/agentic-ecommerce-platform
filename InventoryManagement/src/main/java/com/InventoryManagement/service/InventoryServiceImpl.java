package com.InventoryManagement.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.InventoryManagement.dao.inventoryRepository;
import com.InventoryManagement.dto.InventoryCheckRequest;
import com.InventoryManagement.dto.InventoryReleaseRequest;
import com.InventoryManagement.dto.InventoryReserveRequest;
import com.InventoryManagement.dto.InventoryResponse;
import com.InventoryManagement.model.inventory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

	private final inventoryRepository repository;

	@Override
	public InventoryResponse getInventoryByProductId(int productId) {
		Optional<inventory> invOpt = repository.findByProductId(productId);
		if (invOpt.isEmpty()) {
			return InventoryResponse.builder()
					.productId(productId)
					.availableQuantity(0)
					.reservedQuantity(0)
					.success(false)
					.message("Product is currently out of stock.")
					.build();
		}
		inventory inv = invOpt.get();
		return InventoryResponse.builder()
				.inventoryId(inv.getInventoryId())
				.productId(inv.getProductId())
				.availableQuantity(inv.getAvailableQuantity())
				.reservedQuantity(inv.getReservedQuantity())
				.warehouse(inv.getWarehouse())
				.success(true)
				.message("Stock available")
				.build();
	}

	@Override
	public InventoryResponse checkStock(InventoryCheckRequest checkRequest) {
		Optional<inventory> invOpt = repository.findByProductId(checkRequest.getProductId());
		if (invOpt.isEmpty() || invOpt.get().getAvailableQuantity() < checkRequest.getQuantity()) {
			return InventoryResponse.builder()
					.productId(checkRequest.getProductId())
					.availableQuantity(invOpt.map(inventory::getAvailableQuantity).orElse(0))
					.reservedQuantity(invOpt.map(inventory::getReservedQuantity).orElse(0))
					.success(false)
					.message("Product is currently out of stock.")
					.build();
		}

		inventory inv = invOpt.get();
		return InventoryResponse.builder()
				.inventoryId(inv.getInventoryId())
				.productId(inv.getProductId())
				.availableQuantity(inv.getAvailableQuantity())
				.reservedQuantity(inv.getReservedQuantity())
				.warehouse(inv.getWarehouse())
				.success(true)
				.message("Stock available for quantity: " + checkRequest.getQuantity())
				.build();
	}

	@Override
	@Transactional
	public InventoryResponse reserveStock(InventoryReserveRequest reserveRequest) {
		inventory inv = repository.findByProductId(reserveRequest.getProductId())
				.orElseThrow(() -> new RuntimeException("Product is currently out of stock."));

		if (inv.getAvailableQuantity() < reserveRequest.getQuantity()) {
			throw new RuntimeException("Product is currently out of stock.");
		}

		inv.setAvailableQuantity(inv.getAvailableQuantity() - reserveRequest.getQuantity());
		inv.setReservedQuantity(inv.getReservedQuantity() + reserveRequest.getQuantity());
		inventory saved = repository.save(inv);

		return InventoryResponse.builder()
				.inventoryId(saved.getInventoryId())
				.productId(saved.getProductId())
				.availableQuantity(saved.getAvailableQuantity())
				.reservedQuantity(saved.getReservedQuantity())
				.warehouse(saved.getWarehouse())
				.success(true)
				.message("Stock reserved successfully")
				.build();
	}

	@Override
	@Transactional
	public InventoryResponse releaseStock(InventoryReleaseRequest releaseRequest) {
		inventory inv = repository.findByProductId(releaseRequest.getProductId())
				.orElseThrow(() -> new RuntimeException("Inventory record not found for product: " + releaseRequest.getProductId()));

		int releaseQty = Math.min(inv.getReservedQuantity(), releaseRequest.getQuantity());
		inv.setReservedQuantity(inv.getReservedQuantity() - releaseQty);
		inv.setAvailableQuantity(inv.getAvailableQuantity() + releaseQty);
		inventory saved = repository.save(inv);

		return InventoryResponse.builder()
				.inventoryId(saved.getInventoryId())
				.productId(saved.getProductId())
				.availableQuantity(saved.getAvailableQuantity())
				.reservedQuantity(saved.getReservedQuantity())
				.warehouse(saved.getWarehouse())
				.success(true)
				.message("Stock released successfully")
				.build();
	}

	@Override
	@Transactional
	public InventoryResponse addOrUpdateStock(int productId, int availableQuantity, String warehouse) {
		inventory inv = repository.findByProductId(productId).orElse(null);
		if (inv == null) {
			inv = inventory.builder()
					.productId(productId)
					.availableQuantity(availableQuantity)
					.reservedQuantity(0)
					.warehouse(warehouse != null ? warehouse : "Primary-Warehouse")
					.build();
		} else {
			inv.setAvailableQuantity(inv.getAvailableQuantity() + availableQuantity);
			if (warehouse != null && !warehouse.isEmpty()) {
				inv.setWarehouse(warehouse);
			}
		}

		inventory saved = repository.save(inv);
		return InventoryResponse.builder()
				.inventoryId(saved.getInventoryId())
				.productId(saved.getProductId())
				.availableQuantity(saved.getAvailableQuantity())
				.reservedQuantity(saved.getReservedQuantity())
				.warehouse(saved.getWarehouse())
				.success(true)
				.message("Inventory updated successfully")
				.build();
	}

}
