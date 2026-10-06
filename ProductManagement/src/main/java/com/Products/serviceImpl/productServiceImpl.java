package com.Products.serviceImpl;

import java.util.List;

import com.Products.dto.Response.productResponse;
import com.Products.dto.request.productCreateRequest;
import com.Products.dto.request.productUpdateRequest;

public interface productServiceImpl {

	productResponse addProduct(productCreateRequest productRequest);
	productResponse updateProduct(int id, productUpdateRequest productRequest);
	productResponse getProductById(int id);
	List<productResponse> getAllProducts();
	List<productResponse> searchProducts(String keyword);
	void deleteProduct(int id);

}
