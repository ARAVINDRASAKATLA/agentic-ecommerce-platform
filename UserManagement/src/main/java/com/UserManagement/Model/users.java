package com.UserManagement.Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

	@Column(unique = true)
	private String userName;

	@Column(unique = true)
	private String userEmail;

	private String userPassword;
	
	@Column(unique = true)
	private Long phnNumber;

	// Roles: ROLE_ADMIN, ROLE_CUSTOMER, ROLE_DRIVER
	@Builder.Default
	private String role = "ROLE_CUSTOMER";

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_Id")
	private Address address;

	public users(String userName, String userEmail, String userPassword, Long phnNumber, Address address) {
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.phnNumber = phnNumber;
		this.address = address;
		this.role = "ROLE_CUSTOMER";
	}

	public users(String userName, String userEmail, String userPassword, Long phnNumber) {
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.phnNumber = phnNumber;
		this.role = "ROLE_CUSTOMER";
	}

}
