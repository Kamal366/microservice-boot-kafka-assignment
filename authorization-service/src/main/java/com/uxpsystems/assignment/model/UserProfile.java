package com.uxpsystems.assignment.model;


public class UserProfile {


	private String username;

	private String address;


	//need default constructor for JSON Parsing
	public UserProfile()
	{

	}
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
