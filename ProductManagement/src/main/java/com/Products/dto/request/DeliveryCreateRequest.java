package com.Products.dto.request;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public class DeliveryCreateRequest {
	
	private int orderId;
	private String trackingNumber;
	private String ShippingAddress;
	private LocalDate estimatedDeliveryDate;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "DeliveryCreateRequest [orderId=" + orderId + ", trackingNumber=" + trackingNumber + ", ShippingAddress="
				+ ShippingAddress + ", estimatedDeliveryDate=" + estimatedDeliveryDate + ", status=" + status + "]";
	}
	public DeliveryCreateRequest(int orderId, String trackingNumber, String shippingAddress,
			LocalDate estimatedDeliveryDate, String status) {
		super();
		this.orderId = orderId;
		this.trackingNumber = trackingNumber;
		ShippingAddress = shippingAddress;
		this.estimatedDeliveryDate = estimatedDeliveryDate;
		this.status = status;
	}
	public DeliveryCreateRequest() {
		super();
	}
	
	
	

}
