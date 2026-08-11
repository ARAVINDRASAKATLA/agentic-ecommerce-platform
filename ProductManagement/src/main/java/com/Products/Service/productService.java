package com.Products.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Products.Builder.productBuilder;
import com.Products.Dao.productRepository;
import com.Products.Model.product;
import com.Products.dto.Response.productResponse;
import com.Products.dto.request.productCreateRequest;
import com.Products.serviceImpl.productServiceImpl;

@Service
public class productService implements productServiceImpl {

	@Autowired
	public productRepository productRepository;
	
	@Override
	public productResponse addProduct(productCreateRequest productRequest) {
		
		product productCreatRequest = productBuilder.buildProductCreatRequest(productRequest);
		  productRepository.save(productCreatRequest);
		  
		 return productBuilder.buildProductResponce(productCreatRequest);
		  
	}

	

}
