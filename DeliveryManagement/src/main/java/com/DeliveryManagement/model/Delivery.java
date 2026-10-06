package com.DeliveryManagement.model;

import java.time.LocalDate;

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
@Table(name = "deliveries")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Delivery {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int deliveryId;
	private int orderId;
	private Integer customerId;
	private Integer driverId;
	private Integer vehicleId;
	private String trackingNumber;
	private String pickupLocation;
	private String deliveryLocation;
	private String shippingAddress;
	private LocalDate estimatedDeliveryDate;
	private LocalDate actualDeliveryDate;
	private String status;

	// Backward compatibility alias for shippingAddress <-> deliveryLocation
	public String getShippingAddress() {
		return (shippingAddress != null && !shippingAddress.isEmpty()) ? shippingAddress : deliveryLocation;
	}

	public void setShippingAddress(String address) {
		this.shippingAddress = address;
		if (this.deliveryLocation == null || this.deliveryLocation.isEmpty()) {
			this.deliveryLocation = address;
		}
	}
}