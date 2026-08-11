package com.UserManagement.RequestDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class newUserCreateRequest {


	private String userName;

	private String userEmail;

	private String userPassword;

	private Long phnNumber;

	private UserAddressCreateRequest address;


}
