package com.uxpsystems.assignment.profileservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uxpsystems.assignment.profileservice.model.UserProfile;
import com.uxpsystems.assignment.profileservice.repository.UserProfileRepository;

@Service
public class UserProfileServiceImpl implements UserProfileService {

	@Autowired
	private UserProfileRepository userProfileRepository;


	/**
	 * get all user data
	 */
	@Override
	public List<UserProfile> findAll() {
		return userProfileRepository.findAll();
	}

	/**
	 * find user profile by username
	 */
	@Override
	public UserProfile findByUsername(String userName) {

		return userProfileRepository.findByUsername(userName);
	}

	/**
	 * method to save user profile data
	 */
	@Override
	public UserProfile save(UserProfile userProfile) {
		return userProfileRepository.save(userProfile);
	}

	/**
	 * method to update user
	 */
	public UserProfile updateUser(UserProfile userProfile) {

		String userName = userProfile.getUsername();

		UserProfile editUser = userProfileRepository.findByUsername(userName);

		//editUser.setUsername(userName);
		editUser.setAddress(userProfile.getAddress());
		editUser.setMobile(userProfile.getMobile());

		return userProfileRepository.save(userProfile);
	}


	/**
	 * delete user method
	 */
	@Override
	public void deleteUser(String userName) {

		UserProfile userProfile = userProfileRepository.findByUsername(userName);

		userProfileRepository.delete(userProfile);

	}

	/**
	 * method to find user by mobile number
	 */
	@Override
	public UserProfile findUserByMobile(String mobile) {
		return userProfileRepository.findByMobile(mobile);
	}

}
