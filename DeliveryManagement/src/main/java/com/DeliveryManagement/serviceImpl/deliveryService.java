package com.DeliveryManagement.serviceImpl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.DeliveryManagement.Dao.DriverRepository;
import com.DeliveryManagement.Dao.VehicleRepository;
import com.DeliveryManagement.Dao.deliveryRepository;
import com.DeliveryManagement.dtoRequest.DeliveryCreateRequest;
import com.DeliveryManagement.dtoRequest.updateDeliveryRequest;
import com.DeliveryManagement.dtoResponse.DeliveryResponse;
import com.DeliveryManagement.model.Delivery;
import com.DeliveryManagement.model.Driver;
import com.DeliveryManagement.model.Vehicle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class deliveryService implements deliveryServiceImpl {
	
	private static final Logger log = LoggerFactory.getLogger(deliveryService.class);

	@Autowired
	private deliveryRepository deliveryRepository;

	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private com.DeliveryManagement.kafka.DeliveryEventProducer deliveryEventProducer;

	@Override
	@Transactional
	public DeliveryResponse deliveries(DeliveryCreateRequest createRequest) {
		Delivery delivery = Delivery.builder()
				.orderId(createRequest.getOrderId())
				.trackingNumber(createRequest.getTrackingNumber())
				.shippingAddress(createRequest.getShippingAddress())
				.deliveryLocation(createRequest.getShippingAddress())
				.pickupLocation("Central Fulfillment Warehouse - Bay 4")
				.estimatedDeliveryDate(createRequest.getEstimatedDeliveryDate())
				.status("CREATED")
				.build();

		// Auto-assign first AVAILABLE driver if present
		Optional<Driver> availableDriverOpt = driverRepository.findFirstByStatus("AVAILABLE");
		if (availableDriverOpt.isPresent()) {
			Driver driver = availableDriverOpt.get();
			delivery.setDriverId(driver.getDriverId());
			delivery.setStatus("DRIVER_ASSIGNED");

			Optional<Vehicle> vehicleOpt = vehicleRepository.findByDriverId(driver.getDriverId());
			vehicleOpt.ifPresent(vehicle -> delivery.setVehicleId(vehicle.getVehicleId()));

			driver.setStatus("ASSIGNED");
			driverRepository.save(driver);
		}
		   
		Delivery savedDelivery = deliveryRepository.save(delivery);

		// Publish DELIVERY_CREATED event to Kafka
		try {
			deliveryEventProducer.publishDeliveryEvent(com.DeliveryManagement.dto.event.DeliveryEvent.builder()
					.deliveryId(savedDelivery.getDeliveryId())
					.orderId(savedDelivery.getOrderId())
					.driverId(savedDelivery.getDriverId())
					.status(savedDelivery.getStatus())
					.trackingNumber(savedDelivery.getTrackingNumber())
					.timestamp(java.time.LocalDateTime.now())
					.build());
		} catch (Exception ex) {
			log.warn("Could not publish delivery creation to Kafka: {}", ex.getMessage());
		}

		return mapToResponse(savedDelivery);
	}

	@Override
	@Transactional
	public DeliveryResponse updateDelivery(updateDeliveryRequest updateDelivery) {
		Delivery existingDelivery = null;
		if (updateDelivery.getOrderId() > 0) {
			existingDelivery = deliveryRepository.findByOrderId(updateDelivery.getOrderId()).orElse(null);
		}
		if (existingDelivery == null && updateDelivery.getTrackingNumber() != null) {
			existingDelivery = deliveryRepository.findByTrackingNumber(updateDelivery.getTrackingNumber()).orElse(null);
		}

		if (existingDelivery == null) {
			existingDelivery = Delivery.builder()
					.orderId(updateDelivery.getOrderId())
					.trackingNumber(updateDelivery.getTrackingNumber())
					.build();
		}

		if (updateDelivery.getTrackingNumber() != null) {
			existingDelivery.setTrackingNumber(updateDelivery.getTrackingNumber());
		}
		if (updateDelivery.getShippingAddress() != null) {
			existingDelivery.setShippingAddress(updateDelivery.getShippingAddress());
			existingDelivery.setDeliveryLocation(updateDelivery.getShippingAddress());
		}
		if (updateDelivery.getEstimatedDeliveryDate() != null) {
			existingDelivery.setEstimatedDeliveryDate(updateDelivery.getEstimatedDeliveryDate());
		}
		if (updateDelivery.getActualDeliveryDate() != null) {
			existingDelivery.setActualDeliveryDate(updateDelivery.getActualDeliveryDate());
		}
		if (updateDelivery.getStatus() != null) {
			existingDelivery.setStatus(updateDelivery.getStatus());
		}

		Delivery saved = deliveryRepository.save(existingDelivery);

		// Publish delivery update event to Kafka
		try {
			deliveryEventProducer.publishDeliveryEvent(com.DeliveryManagement.dto.event.DeliveryEvent.builder()
					.deliveryId(saved.getDeliveryId())
					.orderId(saved.getOrderId())
					.driverId(saved.getDriverId())
					.status(saved.getStatus())
					.trackingNumber(saved.getTrackingNumber())
					.timestamp(java.time.LocalDateTime.now())
					.build());
		} catch (Exception ex) {
			log.warn("Could not publish delivery update to Kafka: {}", ex.getMessage());
		}

		return mapToResponse(saved);
	}

	@Override
	@Transactional
	public DeliveryResponse assignDriver(int deliveryId, int driverId) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
				.orElseThrow(() -> new RuntimeException("Delivery not found with id: " + deliveryId));
		Driver driver = driverRepository.findById(driverId)
				.orElseThrow(() -> new RuntimeException("Driver not found with id: " + driverId));

		delivery.setDriverId(driver.getDriverId());
		delivery.setStatus("DRIVER_ASSIGNED");

		Optional<Vehicle> vehicleOpt = vehicleRepository.findByDriverId(driver.getDriverId());
		vehicleOpt.ifPresent(v -> delivery.setVehicleId(v.getVehicleId()));

		driver.setStatus("ASSIGNED");
		driverRepository.save(driver);

		Delivery saved = deliveryRepository.save(delivery);
		return mapToResponse(saved);
	}

	@Override
	@Transactional
	public DeliveryResponse updateStatus(int deliveryId, String status) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
				.orElseThrow(() -> new RuntimeException("Delivery not found with id: " + deliveryId));
		delivery.setStatus(status.toUpperCase());
		
		if ("DELIVERED".equalsIgnoreCase(status)) {
			delivery.setActualDeliveryDate(LocalDate.now());
			// Release driver back to AVAILABLE
			if (delivery.getDriverId() != null && delivery.getDriverId() > 0) {
				driverRepository.findById(delivery.getDriverId()).ifPresent(d -> {
					d.setStatus("AVAILABLE");
					driverRepository.save(d);
				});
			}
		} else if ("OUT_FOR_DELIVERY".equalsIgnoreCase(status)) {
			if (delivery.getDriverId() != null && delivery.getDriverId() > 0) {
				driverRepository.findById(delivery.getDriverId()).ifPresent(d -> {
					d.setStatus("ON_TRIP");
					driverRepository.save(d);
				});
			}
		}

		Delivery saved = deliveryRepository.save(delivery);
		return mapToResponse(saved);
	}

	@Override
	public DeliveryResponse getDeliveryByOrderId(int orderId) {
		Delivery delivery = deliveryRepository.findByOrderId(orderId)
				.orElseThrow(() -> new RuntimeException("Delivery not found for order id: " + orderId));
		return mapToResponse(delivery);
	}

	@Override
	public DeliveryResponse getDeliveryById(int deliveryId) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
				.orElseThrow(() -> new RuntimeException("Delivery not found with id: " + deliveryId));
		return mapToResponse(delivery);
	}

	private DeliveryResponse mapToResponse(Delivery d) {
		return DeliveryResponse.builder()
				.deliveryId(d.getDeliveryId())
				.orderId(d.getOrderId())
				.customerId(d.getCustomerId())
				.driverId(d.getDriverId())
				.vehicleId(d.getVehicleId())
				.trackingNumber(d.getTrackingNumber())
				.pickupLocation(d.getPickupLocation())
				.deliveryLocation(d.getDeliveryLocation())
				.shippingAddress(d.getShippingAddress())
				.estimatedDeliveryDate(d.getEstimatedDeliveryDate())
				.actualDeliveryDate(d.getActualDeliveryDate())
				.status(d.getStatus())
				.build();
	}

}
