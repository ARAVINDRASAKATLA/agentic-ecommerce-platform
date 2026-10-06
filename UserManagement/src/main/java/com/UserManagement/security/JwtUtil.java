package com.UserManagement.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${jwt.secret:SuperSecretDefaultKeyForDevelopmentOnly12345678901234567890}")
	private String secret;

	private Key getSigningKey() {
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		// Ensure minimum 256 bits (32 bytes) for HMAC-SHA
		if (keyBytes.length < 32) {
			byte[] padded = new byte[32];
			System.arraycopy(keyBytes, 0, padded, 0, keyBytes.length);
			return Keys.hmacShaKeyFor(padded);
		}
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(int userId, String userName, String role) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", userId);
		claims.put("role", role);
		return createToken(claims, userName);
	}

	private String createToken(Map<String, Object> claims, String subject) {
		long validityMs = 24 * 60 * 60 * 1000; // 24 hours
		return Jwts.builder()
				.claims(claims)
				.subject(subject)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + validityMs))
				.signWith(getSigningKey())
				.compact();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public String extractRole(String token) {
		return extractAllClaims(token).get("role", String.class);
	}

	public Integer extractUserId(String token) {
		return extractAllClaims(token).get("userId", Integer.class);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, String username) {
		final String extractedUser = extractUsername(token);
		return (extractedUser.equals(username) && !isTokenExpired(token));
	}

	public Boolean validateTokenOnly(String token) {
		try {
			return !isTokenExpired(token);
		} catch (Exception e) {
			return false;
		}
	}
}
