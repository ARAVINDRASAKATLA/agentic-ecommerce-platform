package com.Products.serviceImpl;

import com.Products.dto.Response.productResponse;
import com.Products.dto.request.productCreateRequest;

public interface productServiceImpl {

	  public productResponse addProduct(productCreateRequest productRequest);

}
