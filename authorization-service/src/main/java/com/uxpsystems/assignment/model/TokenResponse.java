package com.uxpsystems.assignment.model;

import java.io.Serializable;

public class TokenResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String token;

	public TokenResponse(String jwttoken) {
		this.token = jwttoken;
	}

	public String getToken() {
		return this.token;
	}
}