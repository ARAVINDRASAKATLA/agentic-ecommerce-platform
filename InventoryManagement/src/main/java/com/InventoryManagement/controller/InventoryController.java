package com.InventoryManagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.InventoryManagement.dto.InventoryCheckRequest;
import com.InventoryManagement.dto.InventoryReleaseRequest;
import com.InventoryManagement.dto.InventoryReserveRequest;
import com.InventoryManagement.dto.InventoryResponse;
import com.InventoryManagement.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

	private final InventoryService inventoryService;

	@GetMapping("/{productId}")
	public ResponseEntity<InventoryResponse> getInventory(@PathVariable("productId") int productId) {
		return ResponseEntity.ok(inventoryService.getInventoryByProductId(productId));
	}

	@PostMapping("/check")
	public ResponseEntity<InventoryResponse> checkStock(@RequestBody InventoryCheckRequest checkRequest) {
		InventoryResponse response = inventoryService.checkStock(checkRequest);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/reserve")
	public ResponseEntity<InventoryResponse> reserveStock(@RequestBody InventoryReserveRequest reserveRequest) {
		try {
			InventoryResponse response = inventoryService.reserveStock(reserveRequest);
			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(InventoryResponse.builder()
					.productId(reserveRequest.getProductId())
					.success(false)
					.message(ex.getMessage())
					.build());
		}
	}

	@PostMapping("/release")
	public ResponseEntity<InventoryResponse> releaseStock(@RequestBody InventoryReleaseRequest releaseRequest) {
		InventoryResponse response = inventoryService.releaseStock(releaseRequest);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/add")
	public ResponseEntity<InventoryResponse> addStock(@RequestParam("productId") int productId,
													  @RequestParam("quantity") int quantity,
													  @RequestParam(value = "warehouse", defaultValue = "Hyderabad-Hub") String warehouse) {
		InventoryResponse response = inventoryService.addOrUpdateStock(productId, quantity, warehouse);
		return ResponseEntity.ok(response);
	}

}
