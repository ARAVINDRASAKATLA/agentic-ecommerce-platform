package com.UserManagement.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int addressId;
	private String Street;
	private String city;
	private String pincode;
	private String state;
	private String country;
	
	
	

}
