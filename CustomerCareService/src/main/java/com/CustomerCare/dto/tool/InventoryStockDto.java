package com.CustomerCare.dto.tool;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryStockDto {
	private int productId;
	private int availableQuantity;
	private int quantity;
	private int reservedQuantity;
	private String warehouseLocation;
	private boolean inStock;
}
