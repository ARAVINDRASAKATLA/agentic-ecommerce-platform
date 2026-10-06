package com.CustomerCare.dto.gemini;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeminiRequest {

	@JsonProperty("system_instruction")
	private SystemInstruction systemInstruction;

	private List<Content> contents;

	private List<Tool> tools;
}
