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


	@Override
	public List<UserProfile> findAll() {
		return userProfileRepository.findAll();
	}

	@Override
	public UserProfile findByUsername(String userName) {

		return userProfileRepository.findByUsername(userName);
	}

	@Override
	public UserProfile save(UserProfile userProfile) {
		return userProfileRepository.save(userProfile);
	}

	public UserProfile updateUser(UserProfile userProfile) {

		String userName = userProfile.getUsername();

		UserProfile editUser = userProfileRepository.findByUsername(userName);

		//editUser.setUsername(userName);
		editUser.setAddress(userProfile.getAddress());
		editUser.setMobile(userProfile.getMobile());

		return userProfileRepository.save(userProfile);
	}


	@Override
	public void deleteUser(String userName) {

		UserProfile userProfile = userProfileRepository.findByUsername(userName);

		userProfileRepository.delete(userProfile);

	}

	@Override
	public UserProfile findUserByMobile(String mobile) {
		return userProfileRepository.findByMobile(mobile);
	}

}
