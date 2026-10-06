package com.UserManagement.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.UserManagement.Model.users;
import com.UserManagement.RequestDTO.UpdateUserRequest;
import com.UserManagement.RequestDTO.newUserCreateRequest;
import com.UserManagement.ResponceDTO.userResponse;
import com.UserManagement.Service.userService;
import com.UserManagement.security.AuthRequest;
import com.UserManagement.security.AuthResponse;

@RestController
@RequestMapping("/users")
public class userController {
	
	@Autowired
	public userService userService;
	
	@PostMapping("/register")
	public ResponseEntity<userResponse> registerUser(@RequestBody newUserCreateRequest userCreateRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userCreateRequest));
	}

	@PostMapping("/createUser")
	public ResponseEntity<userResponse> saveUser(@RequestBody newUserCreateRequest userCreateRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userCreateRequest));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
		try {
			AuthResponse response = userService.login(authRequest);
			return ResponseEntity.ok(response);
		} catch (RuntimeException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
		}
	}

	@GetMapping("/validate")
	public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
		try {
			return ResponseEntity.ok(userService.validateToken(token));
		} catch (RuntimeException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<users> updateUser(@PathVariable("id") int id, @RequestBody UpdateUserRequest updateUserRequest) {
		return ResponseEntity.ok(userService.updateUser(id, updateUserRequest));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<users> getUser(@PathVariable("id") int id) {
		return ResponseEntity.ok(userService.getUser(id));
	}
	
	@GetMapping("/getall")
	public ResponseEntity<List<userResponse>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}
	 
	@DeleteMapping("/deleteUser/{id}")
	public ResponseEntity<users> deleteUser(@PathVariable("id") int id) {
		return ResponseEntity.ok(userService.deleteUser(id));
	}

}
