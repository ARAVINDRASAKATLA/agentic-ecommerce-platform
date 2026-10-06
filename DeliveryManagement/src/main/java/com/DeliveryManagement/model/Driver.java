package com.DeliveryManagement.model;

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
@Table(name = "drivers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int driverId;
	private String name;
	private String phone;
	
	// AVAILABLE, ASSIGNED, ON_TRIP, OFFLINE
	private String status;

}
