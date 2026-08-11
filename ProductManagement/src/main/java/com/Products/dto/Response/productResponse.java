package com.Products.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class productResponse {
	
    private String productName;
	
	private String productDescription;
	private String productBrand;
	private double price;
	private double rating;

}
