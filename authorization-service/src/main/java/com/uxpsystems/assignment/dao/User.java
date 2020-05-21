package com.uxpsystems.assignment.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name="USERS")
@EntityListeners(AuditingEntityListener.class)
public class User {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column
	private String username;
	@Column
	@JsonIgnore
	private String password;


	public User(){}

	/**
	 * @param username
	 * @param password
	 * @param enabled
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param lastName the lastname to set
	 */
	public void setPassword(String lastName) {
		this.password = lastName;
	}

	/**
	 * to string method
	 */
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + "]";
	}


}
