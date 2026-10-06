package com.CustomerCare.dto.event;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformEvent {
	private String eventId;
	private String eventType;
	private String topic;
	private Integer orderId;
	private Integer deliveryId;
	private Integer driverId;
	private String payload;
	@Builder.Default
	private LocalDateTime receivedAt = LocalDateTime.now();
}
