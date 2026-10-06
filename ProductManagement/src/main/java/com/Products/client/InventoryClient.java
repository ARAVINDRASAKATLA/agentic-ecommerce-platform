package com.Products.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.Products.dto.feign.InventoryCheckRequestDto;
import com.Products.dto.feign.InventoryReserveRequestDto;
import com.Products.dto.feign.InventoryResponseDto;

@FeignClient(name = "InventoryManagement")
public interface InventoryClient {

	@GetMapping("/inventory/{productId}")
	InventoryResponseDto getInventory(@PathVariable("productId") int productId);

	@PostMapping("/inventory/check")
	InventoryResponseDto checkStock(@RequestBody InventoryCheckRequestDto checkRequest);

	@PostMapping("/inventory/reserve")
	InventoryResponseDto reserveStock(@RequestBody InventoryReserveRequestDto reserveRequest);

	@PostMapping("/inventory/release")
	InventoryResponseDto releaseStock(@RequestBody InventoryReserveRequestDto releaseRequest);

}
