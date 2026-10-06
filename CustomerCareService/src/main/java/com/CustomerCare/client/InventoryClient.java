package com.CustomerCare.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.CustomerCare.dto.tool.InventoryStockDto;

@FeignClient(name = "InventoryManagement", url = "${inventory.service.url:http://localhost:8083}")
public interface InventoryClient {

	@GetMapping("/inventory/{productId}")
	InventoryStockDto getInventory(@PathVariable("productId") int productId);
}
