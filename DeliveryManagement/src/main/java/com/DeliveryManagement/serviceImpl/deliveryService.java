package com.DeliveryManagement.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DeliveryManagement.Dao.deliveryRepository;
import com.DeliveryManagement.dtoRequest.DeliveryCreateRequest;
import com.DeliveryManagement.dtoRequest.updateDeliveryRequest;
import com.DeliveryManagement.dtoResponse.DeliveryResponse;
import com.DeliveryManagement.model.Delivery;

@Service
public class deliveryService  implements deliveryServiceImpl{
	
	@Autowired
	deliveryRepository deliveryRepository;

	@Override
	public DeliveryResponse deliveries(DeliveryCreateRequest createRequest) {
		// TODO Auto-generated method stub
		   Delivery delivery = Delivery.builder()
		     .orderId(createRequest.getOrderId())
		   .trackingNumber(createRequest.getTrackingNumber())
		   .shippingAddress(createRequest.getShippingAddress())
		   .estimatedDeliveryDate(createRequest.getEstimatedDeliveryDate())
		   .status(createRequest.getStatus()).build();
		   
		   Delivery savedDelivery = deliveryRepository.save(delivery);
		   
		   DeliveryResponse deliveryResponse = DeliveryResponse.builder()
		   .deliveryId(savedDelivery.getDeliveryId())
		   .orderId(savedDelivery.getOrderId())
		   .trackingNumber(savedDelivery.getTrackingNumber())
		   .shippingAddress(savedDelivery.getShippingAddress())
		   .estimatedDeliveryDate(savedDelivery.getEstimatedDeliveryDate())
		   .actualDeliveryDate(savedDelivery.getActualDeliveryDate())
		   .status(savedDelivery.getStatus()).build();
		   
		return deliveryResponse;
	}

	@Override
	public DeliveryResponse updateDelivery(updateDeliveryRequest updateDelivery) {
		// TODO Auto-generated method stub
		Delivery delivery = Delivery.builder()
		.trackingNumber(updateDelivery.getTrackingNumber())
		.shippingAddress(updateDelivery.getShippingAddress())
		.estimatedDeliveryDate(updateDelivery.getEstimatedDeliveryDate())
		.actualDeliveryDate(updateDelivery.getActualDeliveryDate())
		.status(updateDelivery.getStatus()).build();
		
		Delivery save = deliveryRepository.save(delivery);
		
		DeliveryResponse deliveryResponse = DeliveryResponse.builder()
		.deliveryId(save.getDeliveryId())
		.orderId(save.getOrderId())
		.trackingNumber(save.getShippingAddress())
		.shippingAddress(save.getShippingAddress())
		.estimatedDeliveryDate(save.getEstimatedDeliveryDate())
		.actualDeliveryDate(save.getActualDeliveryDate()).status(save.getStatus())
		.build();
		
		return deliveryResponse;
	}

}
