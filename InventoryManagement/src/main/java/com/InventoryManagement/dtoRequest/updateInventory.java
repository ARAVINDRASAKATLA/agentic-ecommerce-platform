package com.InventoryManagement.dtoRequest;

public class updateInventory {
	

	private int availableQuantity;
	private int reverseQuantity;
	private String warehouse;
	
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
	@Override
	public String toString() {
		return "updateInventory [ availableQuantity=" + availableQuantity
				+ ", reverseQuantity=" + reverseQuantity + ", warehouse=" + warehouse + "]";
	}
	public updateInventory(int availableQuantity, int reverseQuantity, String warehouse) {
		super();

		this.availableQuantity = availableQuantity;
		this.reverseQuantity = reverseQuantity;
		this.warehouse = warehouse;
	}
	public updateInventory() {
		super();
	}
	
	

}
