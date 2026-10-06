package com.InventoryManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
	private int inventoryId;
	private int productId;
	private int availableQuantity;
	private int reservedQuantity;
	private String warehouse;
	private boolean success;
	private String message;
}
