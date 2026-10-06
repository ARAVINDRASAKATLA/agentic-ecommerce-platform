package com.Products.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Products.Service.productService;
import com.Products.dto.Response.productResponse;
import com.Products.dto.request.productCreateRequest;
import com.Products.dto.request.productUpdateRequest;

@RestController
@RequestMapping("/product")
public class productController {
	
	@Autowired
	public productService productService;
	
	// Preserves existing endpoint
	@PostMapping("/addProduct")
	public productResponse addProduct(@RequestBody productCreateRequest productRequest) {
		return productService.addProduct(productRequest);
	}

	@PostMapping
	public productResponse createProduct(@RequestBody productCreateRequest productRequest) {
		return productService.addProduct(productRequest);
	}

	@GetMapping("/{id}")
	public ResponseEntity<productResponse> getProduct(@PathVariable("id") int id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}

	@GetMapping("/all")
	public ResponseEntity<List<productResponse>> getAllProducts() {
		return ResponseEntity.ok(productService.getAllProducts());
	}

	@GetMapping("/search")
	public ResponseEntity<List<productResponse>> searchProducts(@RequestParam("keyword") String keyword) {
		return ResponseEntity.ok(productService.searchProducts(keyword));
	}

	@PutMapping("/{id}")
	public ResponseEntity<productResponse> updateProduct(@PathVariable("id") int id, @RequestBody productUpdateRequest productRequest) {
		return ResponseEntity.ok(productService.updateProduct(id, productRequest));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") int id) {
		productService.deleteProduct(id);
		return ResponseEntity.ok("Product deleted successfully with ID: " + id);
	}

}
