package com.CustomerCare.dto.gemini;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tool {

	@JsonProperty("function_declarations")
	private List<FunctionDeclaration> functionDeclarations;
}
