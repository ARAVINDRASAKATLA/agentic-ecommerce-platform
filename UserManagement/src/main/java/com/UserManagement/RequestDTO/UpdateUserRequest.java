package com.UserManagement.RequestDTO;


public class UpdateUserRequest {

	private String userName;

	private String userEmail;

	private String userPassword;

	private Long phnNumber;

	private UpdateUserAddressRequest address;

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

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Long getPhnNumber() {
		return phnNumber;
	}

	public void setPhnNumber(Long phnNumber) {
		this.phnNumber = phnNumber;
	}

	public UpdateUserAddressRequest getAddress() {
		return address;
	}

	public void setAddress(UpdateUserAddressRequest address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "UpdateUserRequest [userName=" + userName + ", userEmail=" + userEmail + ", userPassword=" + userPassword
				+ ", phnNumber=" + phnNumber + ", address=" + address + "]";
	}

	public UpdateUserRequest(String userName, String userEmail, String userPassword, Long phnNumber,
			UpdateUserAddressRequest address) {
		super();
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.phnNumber = phnNumber;
		this.address = address;
	}

	public UpdateUserRequest(String userName, String userEmail, String userPassword, Long phnNumber) {
		super();
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.phnNumber = phnNumber;
	}

	public UpdateUserRequest() {
		super();
	}

}
