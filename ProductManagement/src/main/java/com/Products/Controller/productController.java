package com.Products.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Products.Service.productService;
import com.Products.dto.Response.productResponse;
import com.Products.dto.request.productCreateRequest;

@RestController
@RequestMapping("/product")
public class productController {
	
	@Autowired
	 public productService productService;
	
	@PostMapping("/addProduct")
	public productResponse addProduct(@RequestBody productCreateRequest productRequest) {
		  return productService.addProduct(productRequest);
	}

}
