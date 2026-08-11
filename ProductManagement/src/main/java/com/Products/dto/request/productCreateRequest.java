package com.Products.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class productCreateRequest {

   private String productName;
	
	@Column(length=1000)
	private String productDescription;
	private String productBrand;
	private double price;
	private double rating;
	
	
	
	
}

