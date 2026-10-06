package com.UserManagement.ServiceImpl;

import java.util.List;

import com.UserManagement.Model.users;
import com.UserManagement.RequestDTO.UpdateUserRequest;
import com.UserManagement.RequestDTO.newUserCreateRequest;
import com.UserManagement.ResponceDTO.userResponse;
import com.UserManagement.security.AuthRequest;
import com.UserManagement.security.AuthResponse;

public interface userServiceImpl {
	
	userResponse createUser(newUserCreateRequest userCreateRequest);
	users updateUser(int id, UpdateUserRequest updaterequest);
	users getUser(int id);
	List<userResponse> getAllUsers();
	users deleteUser(int id);
	AuthResponse login(AuthRequest authRequest);
	AuthResponse validateToken(String token);

}
