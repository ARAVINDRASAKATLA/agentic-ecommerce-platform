package com.Tracking.dto.event;

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
public class TrackingEvent implements Serializable {
	private Long orderId;
	private Long driverId;
	private Double latitude;
	private Double longitude;
	private String status;
	private LocalDateTime timestamp;
}
