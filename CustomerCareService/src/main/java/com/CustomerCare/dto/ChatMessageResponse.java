package com.CustomerCare.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
	private String sessionId;
	private String reply;
	private LocalDateTime timestamp;
	private List<String> suggestedActions;
}
