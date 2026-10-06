package com.Tracking.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Tracking.model.TrackingLocation;

@Repository
public interface TrackingRepository extends JpaRepository<TrackingLocation, Integer> {
	Optional<TrackingLocation> findByDriverId(int driverId);
}
