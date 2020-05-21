package com.uxpsystems.assignment.service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.simple.parser.ParseException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.uxpsystems.assignment.dao.User;
import com.uxpsystems.assignment.model.UserDTO;
import com.uxpsystems.assignment.model.UserProfile;

public interface UserService {

	public User findByUsername(String userName);

	public String getProfile(HttpServletRequest request);

	public String deleteProfile(HttpServletRequest request) throws ParseException;

	public String createProfile(HttpServletRequest request, @Valid UserProfile userDetails) throws ParseException;

	public String updateProfile(HttpServletRequest request, @Valid UserProfile userDetails) throws ParseException;

	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

	public Object save(UserDTO user);

}
