package com.DeliveryManagement.dtoRequest;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public class updateDeliveryRequest {

	
	private int orderId;
	private String trackingNumber;
	private String ShippingAddress;
	private LocalDate estimatedDeliveryDate;
	private LocalDate actualDeliveryDate;
	private String status;
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getShippingAddress() {
		return ShippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		ShippingAddress = shippingAddress;
	}
	public LocalDate getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}
	public void setEstimatedDeliveryDate(LocalDate estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}
	public LocalDate getActualDeliveryDate() {
		return actualDeliveryDate;
	}
	public void setActualDeliveryDate(LocalDate actualDeliveryDate) {
		this.actualDeliveryDate = actualDeliveryDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "updateDeliveryRequest [orderId=" + orderId + ", trackingNumber=" + trackingNumber + ", ShippingAddress="
				+ ShippingAddress + ", estimatedDeliveryDate=" + estimatedDeliveryDate + ", actualDeliveryDate="
				+ actualDeliveryDate + ", status=" + status + "]";
	}
	public updateDeliveryRequest(int orderId, String trackingNumber, String shippingAddress,
			LocalDate estimatedDeliveryDate, LocalDate actualDeliveryDate, String status) {
		super();
		this.orderId = orderId;
		this.trackingNumber = trackingNumber;
		ShippingAddress = shippingAddress;
		this.estimatedDeliveryDate = estimatedDeliveryDate;
		this.actualDeliveryDate = actualDeliveryDate;
		this.status = status;
	}
	public updateDeliveryRequest() {
		super();
	}
	
	
}
