package com.CustomerCare.dto.tool;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackingDetailsDto {
	private Long id;
	private Long orderId;
	private Long driverId;
	private Double latitude;
	private Double longitude;
	private String status;
	private LocalDateTime updatedAt;
}
