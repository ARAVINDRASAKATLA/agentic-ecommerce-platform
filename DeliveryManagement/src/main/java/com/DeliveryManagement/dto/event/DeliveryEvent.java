package com.DeliveryManagement.dto.event;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryEvent implements Serializable {
	private Integer deliveryId;
	private Integer orderId;
	private Integer driverId;
	private String status;
	private String trackingNumber;
	private LocalDateTime timestamp;
}
