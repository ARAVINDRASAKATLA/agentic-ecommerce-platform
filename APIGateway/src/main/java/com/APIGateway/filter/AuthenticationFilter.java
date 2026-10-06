package com.APIGateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.APIGateway.util.JwtUtil;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

	@Autowired
	private JwtUtil jwtUtil;

	// Endpoints open to public without authentication
	private static final List<String> OPEN_ENDPOINTS = List.of(
			"/users/login",
			"/users/register",
			"/users/createUser",
			"/users/validate",
			"/eureka",
			"/actuator",
			"/v3/api-docs",
			"/swagger-ui",
			"/care"
	);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String path = request.getURI().getPath();
		HttpMethod method = request.getMethod();

		// CORS preflight requests bypass immediately
		if (HttpMethod.OPTIONS.equals(method)) {
			return chain.filter(exchange);
		}

		// Check for Authorization header
		List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
		if (authHeaders == null || authHeaders.isEmpty()) {
			// If missing and it is an open endpoint (auth, product browsing, care), allow through as guest
			if (isOpenEndpoint(path, method)) {
				return chain.filter(exchange);
			}
			log.warn("Missing Authorization header for request: {}", path);
			return onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
		}

		String authHeader = authHeaders.get(0);
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			log.warn("Invalid Authorization header format for request: {}", path);
			return onError(exchange, "Authorization header must start with Bearer", HttpStatus.UNAUTHORIZED);
		}

		String token = authHeader.substring(7);

		// Validate JWT token
		if (!jwtUtil.validateToken(token)) {
			log.warn("Invalid or expired JWT token for request: {}", path);
			return onError(exchange, "Invalid or expired JWT token", HttpStatus.UNAUTHORIZED);
		}

		// Extract claims
		String username = jwtUtil.extractUsername(token);
		String role = jwtUtil.extractRole(token);
		Integer userId = jwtUtil.extractUserId(token);

		log.info("Request authenticated: user={}, role={}, path={}", username, role, path);

		// Admin-only route guard
		if (path.startsWith("/users/deleteUser") || path.startsWith("/drivers/delete")) {
			if (role == null || (!role.contains("ADMIN") && !role.equalsIgnoreCase("ROLE_ADMIN"))) {
				return onError(exchange, "Access denied: requires ADMIN role", HttpStatus.FORBIDDEN);
			}
		}

		// Mutate request headers to propagate user identity downstream to internal microservices
		ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
				.header("X-User-Id", userId != null ? String.valueOf(userId) : "0")
				.header("X-User-Role", role != null ? role : "ROLE_CUSTOMER")
				.header("X-Username", username != null ? username : "")
				.build();

		return chain.filter(exchange.mutate().request(mutatedRequest).build());
	}

	private boolean isOpenEndpoint(String path, HttpMethod method) {
		// CORS preflight requests
		if (HttpMethod.OPTIONS.equals(method)) {
			return true;
		}
		for (String openPath : OPEN_ENDPOINTS) {
			if (path.startsWith(openPath)) {
				return true;
			}
		}
		// Public read-only access to products
		if (HttpMethod.GET.equals(method) && path.startsWith("/product")) {
			return true;
		}
		return false;
	}

	private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		String json = String.format("{\"error\":\"%s\",\"status\":%d}", message, httpStatus.value());
		DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
		return response.writeWith(Mono.just(buffer));
	}

	@Override
	public int getOrder() {
		return -1; // High priority before routing
	}
}
