package com.Products.dto.Response;

import lombok.Builder;

@Builder
public class AddressResponse {

	private int addressId;
	private String Street;
	private String city;
	private String pincode;
	private String state;
	private String country;

	@Override
	public String toString() {
		return "AddressResponse [addressId=" + addressId + ", Street=" + Street + ", city=" + city + ", pincode="
				+ pincode + ", state=" + state + ", country=" + country + "]";
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

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

	public AddressResponse(int addressId, String street, String city, String pincode, String state, String country) {
		super();
		this.addressId = addressId;
		Street = street;
		this.city = city;
		this.pincode = pincode;
		this.state = state;
		this.country = country;
	}

}
