package com.Products.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.Products.dto.Response.userResponse;

@FeignClient(name = "UserManagement")
public interface UserClient {

	@GetMapping("/users/{id}")
	userResponse getUser(@PathVariable("id") int id);

}
