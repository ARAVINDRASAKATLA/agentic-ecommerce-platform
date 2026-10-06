package com.UserManagement.Builder;

import com.UserManagement.Model.Address;
import com.UserManagement.Model.users;
import com.UserManagement.RequestDTO.UpdateUserAddressRequest;
import com.UserManagement.RequestDTO.UserAddressCreateRequest;
import com.UserManagement.RequestDTO.newUserCreateRequest;
import com.UserManagement.ResponceDTO.AddressResponse;
import com.UserManagement.ResponceDTO.userResponse;

public class userBuilder {

	public static users buildUserFromCreateRequest(newUserCreateRequest createRequest) {
		

		users user = users.builder()
				.userName(createRequest.getUserName())
				.userPassword(createRequest.getUserPassword())
				.userEmail(createRequest.getUserEmail())
				.phnNumber(createRequest.getPhnNumber())
				.address(buildUserAddressFromCreate(createRequest.getAddress()))
				.build();

		return user;
	}

	public static Address buildUserAddressFromCreate(UserAddressCreateRequest userAddressCreateRequest) {
		Address address = Address.builder().Street(userAddressCreateRequest.getStreet())
				.city(userAddressCreateRequest.getCity()).pincode(userAddressCreateRequest.getPincode())
				.state(userAddressCreateRequest.getState()).country(userAddressCreateRequest.getCountry()).build();

		return address;

	}

	public static userResponse buildUserResponceFromUser(users user) {
		return userResponse.builder()
				.userId(user.getUserId())
				.userName(user.getUserName())
				.userEmail(user.getUserEmail())
				.phnNumber(user.getPhnNumber())
				.role(user.getRole())
				.address(buildAddressResponseFromAddress(user.getAddress()))
				.build();
	}

	public static AddressResponse buildAddressResponseFromAddress(Address address) {
		AddressResponse addressResponse = AddressResponse.builder()
				.addressId(address.getAddressId())
				.Street(address.getStreet()).city(address.getCity())
				.pincode(address.getPincode()).state(address.getState()).country(address.getCountry()).build();
		return addressResponse;
	}
	
//	public static users buildUserUpdateRequest(UpdateUserRequest updateUserRequest) {
//		return users.builder()
//		   .userName(updateUserRequest.getUserName())
//		   .userPassword(updateUserRequest.getUserPassword())
//		   .userEmail(updateUserRequest.getUserEmail())
//		   .phnNumber(updateUserRequest.getPhnNumber())
//		   .address(buildUserAddressUpdateRequest(updateUserRequest.getAddress())).build();
//	}
//
	public static Address buildUserAddressUpdateRequest(UpdateUserAddressRequest addressRequest) {
		  Address address = Address.builder()
		  .Street(addressRequest.getStreet())
		  .city(addressRequest.getCity())
		  .pincode(addressRequest.getPincode())
		  .state(addressRequest.getState()).build();
		  
		  return address;
		  
	}
}
