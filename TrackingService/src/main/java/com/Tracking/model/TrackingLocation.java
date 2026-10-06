package com.Tracking.model;

import java.time.LocalDateTime;

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
@Table(name = "tracking_locations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingLocation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int locationId;
	private int driverId;
	private double latitude;
	private double longitude;
	private LocalDateTime lastUpdated;

}
