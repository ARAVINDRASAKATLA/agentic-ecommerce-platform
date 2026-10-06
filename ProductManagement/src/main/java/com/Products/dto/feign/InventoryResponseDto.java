package com.Products.dto.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDto {
	private int inventoryId;
	private int productId;
	private int availableQuantity;
	private int reservedQuantity;
	private String warehouse;
	private boolean success;
	private String message;
}
