package com.CustomerCare.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CustomerCare.dto.tool.OrderDetailsDto;
import com.CustomerCare.dto.tool.ProductDetailsDto;

@FeignClient(name = "ProductManagement", url = "${product.service.url:http://localhost:8082}")
public interface ProductClient {

	@GetMapping("/orders/{id}")
	OrderDetailsDto getOrder(@PathVariable("id") int id);

	@GetMapping("/product/{id}")
	ProductDetailsDto getProduct(@PathVariable("id") int id);

	@GetMapping("/product/search")
	List<ProductDetailsDto> searchProducts(@RequestParam("keyword") String keyword);

	@PutMapping("/orders/{id}/cancel")
	OrderDetailsDto cancelOrder(@PathVariable("id") int id);
}
