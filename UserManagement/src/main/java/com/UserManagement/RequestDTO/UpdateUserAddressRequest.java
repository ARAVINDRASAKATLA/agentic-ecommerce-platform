package com.UserManagement.RequestDTO;

import lombok.Builder;



@Builder
public class UpdateUserAddressRequest {
	
	private String Street;
	private String city;
	private String pincode;
	private String state;

	private String country;

	public String getStreet() {
		return Street;
	}

	public void setStreet(String street) {
		Street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "UpdateUserAddressRequest [Street=" + Street + ", city=" + city + ", pincode=" + pincode + ", state="
				+ state + ", country=" + country + "]";
	}

	public UpdateUserAddressRequest(String street, String city, String pincode, String state, String country) {
		super();
		Street = street;
		this.city = city;
		this.pincode = pincode;
		this.state = state;
		this.country = country;
	}

	public UpdateUserAddressRequest() {
		super();
	}
	


}
