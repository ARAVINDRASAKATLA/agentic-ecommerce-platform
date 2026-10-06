package com.CustomerCare.dto.gemini;

import java.util.List;

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
public class GeminiResponse {
	private List<Candidate> candidates;

	public String getFirstCandidateText() {
		if (candidates != null && !candidates.isEmpty()) {
			Candidate candidate = candidates.get(0);
			if (candidate.getContent() != null && candidate.getContent().getParts() != null) {
				StringBuilder sb = new StringBuilder();
				for (Part part : candidate.getContent().getParts()) {
					if (part.getText() != null) {
						sb.append(part.getText());
					}
				}
				return sb.toString();
			}
		}
		return "I am sorry, but I was unable to generate a response at this time. Please try again.";
	}
}
