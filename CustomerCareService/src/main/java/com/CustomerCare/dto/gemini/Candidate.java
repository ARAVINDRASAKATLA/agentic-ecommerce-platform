package com.CustomerCare.dto.gemini;

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
public class Candidate {
	private Content content;
	private String finishReason;

	public FunctionCall getFunctionCall() {
		if (content != null && content.getParts() != null) {
			for (Part p : content.getParts()) {
				if (p.getFunctionCall() != null) {
					return p.getFunctionCall();
				}
			}
		}
		return null;
	}

	public String getText() {
		if (content != null && content.getParts() != null) {
			StringBuilder sb = new StringBuilder();
			for (Part p : content.getParts()) {
				if (p.getText() != null) {
					sb.append(p.getText());
				}
			}
			return sb.toString();
		}
		return "";
	}
}
