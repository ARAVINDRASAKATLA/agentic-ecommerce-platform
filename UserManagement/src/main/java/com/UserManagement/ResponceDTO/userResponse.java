package com.UserManagement.ResponceDTO;

import lombok.Builder;

@Builder
public class userResponse {

	private int userId;
	private String userName;

	private String userEmail;

	private Long phnNumber;

	private AddressResponse address;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Long getPhnNumber() {
		return phnNumber;
	}

	public void setPhnNumber(Long phnNumber) {
		this.phnNumber = phnNumber;
	}

	public AddressResponse getAddress() {
		return address;
	}

	public void setAddress(AddressResponse address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "userResponseDto [userId=" + userId + ", userName=" + userName + ", userEmail=" + userEmail
				+ ", phnNumber=" + phnNumber + ", address=" + address + "]";
	}

	public userResponse(int userId, String userName, String userEmail, Long phnNumber, AddressResponse address) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userEmail = userEmail;
		this.phnNumber = phnNumber;
		this.address = address;
	}

}
