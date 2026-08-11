package com.Products.dto.Response;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public class DeliveryResponse {
	
	private int deliveryId;
	private int orderId;
	private String trackingNumber;
	private String shippingAddress;
	private LocalDate estimatedDeliveryDate;
	private LocalDate actualDeliveryDate;
	private String status;
	public int getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(int deliveryId) {
		this.deliveryId = deliveryId;
	}
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
		return shippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
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
		return "DeliveryResponse [deliveryId=" + deliveryId + ", orderId=" + orderId + ", trackingNumber="
				+ trackingNumber + ", shippingAddress=" + shippingAddress + ", estimatedDeliveryDate="
				+ estimatedDeliveryDate + ", actualDeliveryDate=" + actualDeliveryDate + ", status=" + status + "]";
	}
	public DeliveryResponse(int deliveryId, int orderId, String trackingNumber, String shippingAddress,
			LocalDate estimatedDeliveryDate, LocalDate actualDeliveryDate, String status) {
		super();
		this.deliveryId = deliveryId;
		this.orderId = orderId;
		this.trackingNumber = trackingNumber;
		this.shippingAddress = shippingAddress;
		this.estimatedDeliveryDate = estimatedDeliveryDate;
		this.actualDeliveryDate = actualDeliveryDate;
		this.status = status;
	}
	
	
	
	

}
