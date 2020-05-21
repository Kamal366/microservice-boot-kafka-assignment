package com.uxpsystems.assignment.profileservice.service;

import java.util.List;

import com.uxpsystems.assignment.profileservice.model.UserProfile;

public interface UserProfileService {

	UserProfile save(UserProfile userProfile);

	UserProfile updateUser(UserProfile userProfile);

	UserProfile findByUsername(String userName);

	void deleteUser(String userName);

	UserProfile findUserByMobile(String mobile);

	List<UserProfile> findAll();

}
