package com.DeliveryManagement.serviceImpl;

import com.DeliveryManagement.dtoRequest.DeliveryCreateRequest;
import com.DeliveryManagement.dtoRequest.updateDeliveryRequest;
import com.DeliveryManagement.dtoResponse.DeliveryResponse;

public interface deliveryServiceImpl {
	
	DeliveryResponse deliveries(DeliveryCreateRequest createRequest);
	DeliveryResponse updateDelivery(updateDeliveryRequest deliveryRequest);
	DeliveryResponse getDeliveryByOrderId(int orderId);
	DeliveryResponse getDeliveryById(int deliveryId);
	DeliveryResponse assignDriver(int deliveryId, int driverId);
	DeliveryResponse updateStatus(int deliveryId, String status);

}
