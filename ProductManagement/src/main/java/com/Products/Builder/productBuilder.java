package com.Products.Builder;

import com.Products.Model.product;
import com.Products.dto.Response.productResponse;
import com.Products.dto.request.productCreateRequest;

public class productBuilder {
	
	public static product buildProductCreatRequest(productCreateRequest productRequest) {
		return product.builder()
				.productName(productRequest.getProductName())
				.productDescription(productRequest.getProductDescription())
				.productBrand(productRequest.getProductBrand())
				.price(productRequest.getPrice())
				.build();
	}
	
	public static productResponse buildProductResponce(product product) {
		return productResponse.builder()
				.productId(product.getProductId())
				.productName(product.getProductName())
				.productDescription(product.getProductDescription())
				.productBrand(product.getProductBrand())
				.price(product.getPrice())
				.rating(product.getRating())
				.build();
	}

}
