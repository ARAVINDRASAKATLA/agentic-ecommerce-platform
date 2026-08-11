package com.InventoryManagement.model;

import jakarta.persistence.Entity;
import lombok.Builder;

@Entity
@Builder
public class inventory {
	
	private int inventoryId;
	private int productId;
	private int availableQuantity;
	private int reverseQuantity;
	private String warehouse;
	public int getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(int inventoryId) {
		this.inventoryId = inventoryId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getAvailableQuantity() {
		return availableQuantity;
	}
	public void setAvailableQuantity(int availableQuantity) {
		this.availableQuantity = availableQuantity;
	}
	public int getReverseQuantity() {
		return reverseQuantity;
	}
	public void setReverseQuantity(int reverseQuantity) {
		this.reverseQuantity = reverseQuantity;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	public inventory(int inventoryId, int productId, int availableQuantity, int reverseQuantity, String warehouse) {
		super();
		this.inventoryId = inventoryId;
		this.productId = productId;
		this.availableQuantity = availableQuantity;
		this.reverseQuantity = reverseQuantity;
		this.warehouse = warehouse;
	}
	@Override
	public String toString() {
		return "inventory [inventoryId=" + inventoryId + ", productId=" + productId + ", availableQuantity="
				+ availableQuantity + ", reverseQuantity=" + reverseQuantity + ", warehouse=" + warehouse + "]";
	}
	public inventory(int productId, int availableQuantity, int reverseQuantity, String warehouse) {
		super();
		this.productId = productId;
		this.availableQuantity = availableQuantity;
		this.reverseQuantity = reverseQuantity;
		this.warehouse = warehouse;
	}
	
	

}
