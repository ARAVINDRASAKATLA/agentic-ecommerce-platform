package com.Products.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Products.Builder.productBuilder;
import com.Products.Dao.productRepository;
import com.Products.Model.product;
import com.Products.dto.Response.productResponse;
import com.Products.dto.request.productCreateRequest;
import com.Products.dto.request.productUpdateRequest;
import com.Products.serviceImpl.productServiceImpl;

@Service
public class productService implements productServiceImpl {

	@Autowired
	public productRepository productRepository;
	
	@Override
	public productResponse addProduct(productCreateRequest productRequest) {
		product prod = productBuilder.buildProductCreatRequest(productRequest);
		product saved = productRepository.save(prod);
		return productBuilder.buildProductResponce(saved);
	}

	@Override
	public productResponse updateProduct(int id, productUpdateRequest productRequest) {
		product prod = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
		
		if (productRequest.getProductName() != null) {
			prod.setProductName(productRequest.getProductName());
		}
		if (productRequest.getProductDescription() != null) {
			prod.setProductDescription(productRequest.getProductDescription());
		}
		if (productRequest.getProductBrand() != null) {
			prod.setProductBrand(productRequest.getProductBrand());
		}
		if (productRequest.getPrice() > 0) {
			prod.setPrice(productRequest.getPrice());
		}
		if (productRequest.getRating() > 0) {
			prod.setRating(productRequest.getRating());
		}

		product saved = productRepository.save(prod);
		return productBuilder.buildProductResponce(saved);
	}

	@Override
	public productResponse getProductById(int id) {
		product prod = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
		return productBuilder.buildProductResponce(prod);
	}

	@Override
	public List<productResponse> getAllProducts() {
		return productRepository.findAll().stream()
				.map(productBuilder::buildProductResponce)
				.toList();
	}

	@Override
	public List<productResponse> searchProducts(String keyword) {
		if (keyword == null || keyword.trim().isEmpty()) {
			return getAllProducts();
		}
		return productRepository.findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(keyword, keyword)
				.stream()
				.map(productBuilder::buildProductResponce)
				.toList();
	}

	@Override
	public void deleteProduct(int id) {
		product prod = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
		productRepository.delete(prod);
	}

}
