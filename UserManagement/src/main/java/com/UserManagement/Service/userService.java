package com.UserManagement.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.UserManagement.Builder.userBuilder;
import com.UserManagement.Model.users;
import com.UserManagement.RequestDTO.UpdateUserRequest;
import com.UserManagement.RequestDTO.newUserCreateRequest;
import com.UserManagement.ResponceDTO.userResponse;
import com.UserManagement.ServiceImpl.userServiceImpl;
import com.UserManagement.dao.userRepository;
import com.UserManagement.security.AuthRequest;
import com.UserManagement.security.AuthResponse;
import com.UserManagement.security.JwtUtil;

@Service
public class userService implements userServiceImpl {
	
	@Autowired
	public userRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public userResponse createUser(newUserCreateRequest userCreateRequest) {
		users userData = userBuilder.buildUserFromCreateRequest(userCreateRequest);
		
		// Secure password hashing with BCrypt
		if (userData.getUserPassword() != null) {
			userData.setUserPassword(passwordEncoder.encode(userData.getUserPassword()));
		}
		if (userData.getRole() == null || userData.getRole().trim().isEmpty()) {
			userData.setRole("ROLE_CUSTOMER");
		}

		users saved = repository.save(userData);
		return userBuilder.buildUserResponceFromUser(saved);
	}

	@Override
	public AuthResponse login(AuthRequest authRequest) {
		String identifier = authRequest.getUserNameOrEmail();
		users user = repository.findByUserNameOrUserEmail(identifier, identifier)
				.orElseThrow(() -> new RuntimeException("Invalid username or password"));

		if (!passwordEncoder.matches(authRequest.getPassword(), user.getUserPassword())) {
			throw new RuntimeException("Invalid username or password");
		}

		String token = jwtUtil.generateToken(user.getUserId(), user.getUserName(), user.getRole());

		return AuthResponse.builder()
				.token(token)
				.userId(user.getUserId())
				.userName(user.getUserName())
				.userEmail(user.getUserEmail())
				.role(user.getRole())
				.message("Authentication successful")
				.build();
	}

	@Override
	public AuthResponse validateToken(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		if (!jwtUtil.validateTokenOnly(token)) {
			throw new RuntimeException("Token is invalid or expired");
		}

		String username = jwtUtil.extractUsername(token);
		String role = jwtUtil.extractRole(token);
		Integer userId = jwtUtil.extractUserId(token);

		return AuthResponse.builder()
				.token(token)
				.userId(userId != null ? userId : 0)
				.userName(username)
				.role(role)
				.message("Token is valid")
				.build();
	}

	@Override
	public users updateUser(int id, UpdateUserRequest updaterequest) {
		users user = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
		user.setUserName(updaterequest.getUserName());
		user.setUserEmail(updaterequest.getUserEmail());
		if (updaterequest.getUserPassword() != null && !updaterequest.getUserPassword().isEmpty()) {
			user.setUserPassword(passwordEncoder.encode(updaterequest.getUserPassword()));
		}
		user.setPhnNumber(updaterequest.getPhnNumber());
		if (updaterequest.getAddress() != null) {
			user.setAddress(userBuilder.buildUserAddressUpdateRequest(updaterequest.getAddress()));
		}
		
		return repository.save(user);
	}

	@Override
	public users getUser(int id) {
		return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
	}

	@Override
	public List<userResponse> getAllUsers() {
		return repository.findAll().stream()
				.map(userBuilder::buildUserResponceFromUser)
				.toList();
	}

	@Override
	public users deleteUser(int id) {
		users foundUser = repository.findById(id).orElseThrow(() -> new RuntimeException("No user found with id: " + id));
		repository.delete(foundUser);
		return foundUser;
	}

}
