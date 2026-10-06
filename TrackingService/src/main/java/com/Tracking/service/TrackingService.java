package com.Tracking.service;

import com.Tracking.dto.LocationUpdateRequest;
import com.Tracking.dto.OrderTrackingResponse;
import com.Tracking.model.TrackingLocation;

public interface TrackingService {
	TrackingLocation updateDriverLocation(LocationUpdateRequest request);
	OrderTrackingResponse trackOrder(int orderId);
}
