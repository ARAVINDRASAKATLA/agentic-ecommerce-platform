package com.UserManagement.ServiceImpl;

import java.util.List;

import com.UserManagement.Model.users;
import com.UserManagement.RequestDTO.UpdateUserRequest;
import com.UserManagement.RequestDTO.newUserCreateRequest;
import com.UserManagement.ResponceDTO.userResponse;

public interface userServiceImpl {
	
	public userResponse createUser(newUserCreateRequest userCreateRequest);
	
	public users updateUser(int id, UpdateUserRequest updaterequest);
	
	public users getUser(int id);
	
	public List<userResponse> getAllUsers();
	
	public users deleteUser(int id);

}
