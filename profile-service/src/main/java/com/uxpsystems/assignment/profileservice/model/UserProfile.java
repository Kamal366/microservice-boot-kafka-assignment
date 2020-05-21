package com.uxpsystems.assignment.profileservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;


@Entity
public class UserProfile {


	@Id
	@Column(name = "username", unique = true)
	@Length(min = 1, message = "*Username field can not be empty")
	private String username;
	
	@Column(nullable=false,name = "address")
	@NotNull
	@Length(min = 1, message = "*Address field can not be empty")
	private String address;
	
	@Column(nullable=false, unique = true,name = "mobile")
	@Length(min = 10,max = 10, message = "*Your mobile must have at least 10 number")
	@Pattern(regexp="(^$|[0-9]{10})", message = "Invalid number")
	private String mobile;
	

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
