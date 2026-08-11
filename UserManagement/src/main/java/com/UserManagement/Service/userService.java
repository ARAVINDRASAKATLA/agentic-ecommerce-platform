package com.UserManagement.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.UserManagement.Builder.userBuilder;
import com.UserManagement.Model.users;
import com.UserManagement.RequestDTO.UpdateUserRequest;
import com.UserManagement.RequestDTO.newUserCreateRequest;
import com.UserManagement.ResponceDTO.userResponse;
import com.UserManagement.ServiceImpl.userServiceImpl;
import com.UserManagement.dao.userRepository;

@Service
public class userService implements userServiceImpl{
	
	@Autowired
	 public userRepository repository;

	@Override
	public userResponse createUser(newUserCreateRequest userCreateRequest) {
		
		   users userData = userBuilder.buildUserFromCreateRequest(userCreateRequest);
		   users save = repository.save(userData);
		   userResponse userResponceFromUser = userBuilder.buildUserResponceFromUser(save);
		  
		return userResponceFromUser;
	}

	@Override
	public users updateUser(int id, UpdateUserRequest updaterequest) {
		// TODO Auto-generated method stub
		  users user = repository.findById(id).orElseThrow( () ->new  RuntimeException("user not Found"));
		     user.setUserName(updaterequest.getUserName());
		     user.setUserEmail(updaterequest.getUserEmail());
		     user.setUserPassword(updaterequest.getUserPassword());
		     user.setPhnNumber(updaterequest.getPhnNumber());
		     user.setAddress(userBuilder.buildUserAddressUpdateRequest(updaterequest.getAddress()));
		
		return repository.save(user);
	}

	@Override
	public users getUser(int id) {
		// TODO Auto-generated method stub
		  users user = repository.findById(id).orElseThrow(()-> new RuntimeException("User Not Fount"));
		  
		return user;
	}

	@Override
	public List<userResponse> getAllUsers() {
		// TODO Auto-generated method stub
		return repository.findAll().stream()
		.map(userBuilder::buildUserResponceFromUser).toList();
	
	}

	@Override
	public users deleteUser(int id) {
		// TODO Auto-generated method stub
		users foundUser = repository.findById(id).orElseThrow(()->new RuntimeException("No user Found"));
		     repository.delete(foundUser);
		     
		     return foundUser;
		
	}

}
