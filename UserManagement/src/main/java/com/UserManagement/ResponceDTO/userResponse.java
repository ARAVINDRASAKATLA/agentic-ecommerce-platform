package com.UserManagement.ResponceDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class userResponse {

	private int userId;
	private String userName;
	private String userEmail;
	private Long phnNumber;
	private String role;
	private AddressResponse address;

}
