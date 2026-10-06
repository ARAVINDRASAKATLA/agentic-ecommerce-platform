package com.InventoryManagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class inventory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int inventoryId;
	private int productId;
	private int availableQuantity;
	private int reservedQuantity;
	private String warehouse;

	// Backward compatibility alias methods for legacy code referencing reverseQuantity
	public int getReverseQuantity() {
		return reservedQuantity;
	}

	public void setReverseQuantity(int reverseQuantity) {
		this.reservedQuantity = reverseQuantity;
	}

	public inventory(int productId, int availableQuantity, int reservedQuantity, String warehouse) {
		this.productId = productId;
		this.availableQuantity = availableQuantity;
		this.reservedQuantity = reservedQuantity;
		this.warehouse = warehouse;
	}
}
