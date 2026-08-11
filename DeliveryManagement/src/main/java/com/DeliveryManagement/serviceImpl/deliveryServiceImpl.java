package com.DeliveryManagement.serviceImpl;

import com.DeliveryManagement.dtoRequest.DeliveryCreateRequest;
import com.DeliveryManagement.dtoRequest.updateDeliveryRequest;
import com.DeliveryManagement.dtoResponse.DeliveryResponse;

public interface deliveryServiceImpl {
	
	public DeliveryResponse deliveries(DeliveryCreateRequest createRequest);

	public DeliveryResponse updateDelivery(updateDeliveryRequest deliveryRequest);
}
