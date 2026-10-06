package com.UserManagement.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	private String token;
	private int userId;
	private String userName;
	private String userEmail;
	private String role;
	private String message;
}
