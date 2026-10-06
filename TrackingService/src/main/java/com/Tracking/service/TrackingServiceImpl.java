package com.Tracking.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Tracking.client.DeliveryTrackingClient;
import com.Tracking.dao.TrackingRepository;
import com.Tracking.dto.DeliveryInfoDto;
import com.Tracking.dto.DriverInfoDto;
import com.Tracking.dto.LocationUpdateRequest;
import com.Tracking.dto.OrderTrackingResponse;
import com.Tracking.model.TrackingLocation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {

	private static final Logger log = LoggerFactory.getLogger(TrackingServiceImpl.class);

	private final TrackingRepository trackingRepository;
	private final DeliveryTrackingClient deliveryTrackingClient;
	private final com.Tracking.kafka.TrackingEventProducer trackingEventProducer;

	@Override
	@Transactional
	public TrackingLocation updateDriverLocation(LocationUpdateRequest request) {
		TrackingLocation location = trackingRepository.findByDriverId(request.getDriverId())
				.orElse(null);

		if (location == null) {
			location = TrackingLocation.builder()
					.driverId(request.getDriverId())
					.latitude(request.getLatitude())
					.longitude(request.getLongitude())
					.lastUpdated(LocalDateTime.now())
					.build();
		} else {
			location.setLatitude(request.getLatitude());
			location.setLongitude(request.getLongitude());
			location.setLastUpdated(LocalDateTime.now());
		}

		TrackingLocation saved = trackingRepository.save(location);

		// Publish TrackingEvent to Kafka
		try {
			if (trackingEventProducer != null) {
				trackingEventProducer.publishTrackingEvent(com.Tracking.dto.event.TrackingEvent.builder()
						.driverId((long) saved.getDriverId())
						.latitude(saved.getLatitude())
						.longitude(saved.getLongitude())
						.status("LOCATION_UPDATED")
						.timestamp(saved.getLastUpdated())
						.build());
			}
		} catch (Exception ex) {
			log.warn("Could not publish tracking event to Kafka: {}", ex.getMessage());
		}

		return saved;
	}

	@Override
	public OrderTrackingResponse trackOrder(int orderId) {
		DeliveryInfoDto deliveryInfo = null;
		try {
			deliveryInfo = deliveryTrackingClient.getDeliveryByOrderId(orderId);
		} catch (Exception ex) {
			log.warn("Could not retrieve delivery info for order {}: {}", orderId, ex.getMessage());
		}

		if (deliveryInfo == null) {
			return OrderTrackingResponse.builder()
					.orderId(orderId)
					.deliveryStatus("PENDING")
					.message("Delivery record not yet initialized for order #" + orderId)
					.build();
		}

		Integer driverId = deliveryInfo.getDriverId();
		DriverInfoDto driverInfo = null;
		TrackingLocation location = null;

		if (driverId != null && driverId > 0) {
			try {
				driverInfo = deliveryTrackingClient.getDriverById(driverId);
			} catch (Exception ex) {
				log.warn("Could not retrieve driver info for driver {}: {}", driverId, ex.getMessage());
			}

			location = trackingRepository.findByDriverId(driverId).orElse(null);
		}

		// Fallback default coordinates if driver hasn't sent GPS update yet (Hyderabad Hub coordinates)
		double lat = (location != null) ? location.getLatitude() : 17.3850;
		double lon = (location != null) ? location.getLongitude() : 78.4867;
		LocalDateTime updated = (location != null) ? location.getLastUpdated() : LocalDateTime.now();

		return OrderTrackingResponse.builder()
				.orderId(orderId)
				.deliveryId(deliveryInfo.getDeliveryId())
				.driverId(driverId)
				.driverName(driverInfo != null ? driverInfo.getName() : (driverId != null ? "Assigned Driver #" + driverId : "Unassigned"))
				.driverPhone(driverInfo != null ? driverInfo.getPhone() : "N/A")
				.vehicleNumber(driverInfo != null ? driverInfo.getVehicleNumber() : "N/A")
				.vehicleType(driverInfo != null ? driverInfo.getVehicleType() : "BIKE")
				.latitude(lat)
				.longitude(lon)
				.lastUpdated(updated)
				.deliveryStatus(deliveryInfo.getStatus())
				.pickupLocation(deliveryInfo.getPickupLocation())
				.deliveryLocation(deliveryInfo.getDeliveryLocation())
				.message("Delivery is currently " + deliveryInfo.getStatus())
				.build();
	}

}
