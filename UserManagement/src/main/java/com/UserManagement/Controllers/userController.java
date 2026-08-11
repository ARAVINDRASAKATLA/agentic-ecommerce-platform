package com.UserManagement.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.UserManagement.Model.users;
import com.UserManagement.RequestDTO.UpdateUserRequest;
import com.UserManagement.RequestDTO.newUserCreateRequest;
import com.UserManagement.ResponceDTO.userResponse;
import com.UserManagement.Service.userService;

@RestController
public class userController {
	
	@Autowired
	 public userService userService;
	
	
	@PostMapping("/createUser")
   public userResponse saveUser(@RequestBody newUserCreateRequest userCreatRequest) {
	return userService.createUser(userCreatRequest);
	   
   }
	
	@PutMapping("/update/{id}")
     public users updateUser(@PathVariable("id") int id, @RequestBody UpdateUserRequest updateUserRequest) {
		
    	 return userService.updateUser(id, updateUserRequest);
     }
	
	@GetMapping("/user/{id}")
	public users getUser(@PathVariable("id") int id) {
		return userService.getUser(id);
	}
	
	@GetMapping("/getall")
	public List<userResponse> getAllUsers(){
		 return null;
	}
	 
	@DeleteMapping("/deleteUser/{id}")
	public users deleteUser(@PathVariable("id") int id) {
		return userService.deleteUser(id);
	}
	

}
