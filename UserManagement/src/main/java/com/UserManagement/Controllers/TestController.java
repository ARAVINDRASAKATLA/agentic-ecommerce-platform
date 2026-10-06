package com.UserManagement.Controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestController {
	
	@Value("${jwt.secret:default-secret-key-12345678901234567890}")
	String jwtSecret;
	
	@GetMapping("/secret-status")
	public String getSecretStatus() {
		// Secure health check endpoint: avoids exposing plaintext secret key
		return (jwtSecret != null && !jwtSecret.isEmpty()) ? "SECRET_CONFIGURED" : "SECRET_MISSING";
	}

}
