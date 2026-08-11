package com.InventoryManagement.dtoRequest;

public class creatInventory {
	
	private int productId;
	private int availableQuantity;
	private int reverseQuantity;
	private String warehouse;
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
	public creatInventory(int productId, int availableQuantity, int reverseQuantity, String warehouse) {
		super();
		this.productId = productId;
		this.availableQuantity = availableQuantity;
		this.reverseQuantity = reverseQuantity;
		this.warehouse = warehouse;
	}
	@Override
	public String toString() {
		return "creatInventory [productId=" + productId + ", availableQuantity=" + availableQuantity
				+ ", reverseQuantity=" + reverseQuantity + ", warehouse=" + warehouse + "]";
	}
	public creatInventory() {
		super();
	}
	
	

}
