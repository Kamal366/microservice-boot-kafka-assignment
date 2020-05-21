package com.uxpsystems.assignment.profileservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uxpsystems.assignment.profileservice.model.UserProfile;

import com.uxpsystems.assignment.profileservice.service.UserProfileService;
import com.uxpsystems.assignment.profileservice.constant.ProfileConstants;


@RestController
@RequestMapping("/api/user")
public class ProfileController {


	@Autowired
	private UserProfileService userProfileSerice;


	@GetMapping(ProfileConstants.URL_ALLPROFILE)
	public List<UserProfile> getAllUsers() {
		return userProfileSerice.findAll();
	}
	

	@GetMapping(value=ProfileConstants.URL_PROFILE_UN, produces = MediaType.APPLICATION_JSON_VALUE)
	public  ResponseEntity<Object> getUserById(
			@PathVariable(value = ProfileConstants.USER_NAME) String userName){

		UserProfile user = userProfileSerice.findByUsername(userName);

		if(user==null) {
			return new ResponseEntity<>(new JSONObject().put(ProfileConstants.EXCEPTION,ProfileConstants.PROFILE_NOT_CREATED).toMap(), HttpStatus.OK);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}


	@GetMapping(value=ProfileConstants.URL_PROFILE_MOB, produces = MediaType.APPLICATION_JSON_VALUE)
	public  ResponseEntity<Object> getUserByMobile(
			@PathVariable(value = ProfileConstants.MOBILE) String mobile){

		UserProfile user = userProfileSerice.findUserByMobile(mobile);

		if(user==null) {
			return new ResponseEntity<>(new JSONObject().put(ProfileConstants.NOT_EXISTS,ProfileConstants.MOBILE_NOT_EXISTS).toMap(), HttpStatus.OK);
		}
		return new ResponseEntity<>(new JSONObject().put(ProfileConstants.EXISTS,ProfileConstants.MOBILE_ALREADY_REGISTERED).toMap(), HttpStatus.OK);
	}
	

	@PostMapping(value=ProfileConstants.URL_PROFILE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createProfile(@Valid @RequestBody UserProfile userDetails) {

		UserProfile userNameExists = userProfileSerice.findByUsername(userDetails.getUsername());
		UserProfile userMobileExists = userProfileSerice.findUserByMobile(userDetails.getMobile());

		if (userNameExists != null) {
			return new ResponseEntity<>(new JSONObject().put(ProfileConstants.EXCEPTION,ProfileConstants.USER_HAVE_PROFILE).toMap(), HttpStatus.OK);

		} else if (userMobileExists != null) {
			return new ResponseEntity<>(new JSONObject().put(ProfileConstants.EXCEPTION,ProfileConstants.MOBILE_ALREADY_REGISTERED).toMap(), HttpStatus.OK);
		}

		userProfileSerice.save(userDetails);

		return new ResponseEntity<>(new JSONObject().put(ProfileConstants.SUCCESS,ProfileConstants.PROFILE_CREATED).toMap(), HttpStatus.OK);
	}
	

	@KafkaListener(topics = ProfileConstants.TOPIC_PROFILE_JSON, groupId = ProfileConstants.GROUP_JSON,
			containerFactory = ProfileConstants.LISTENER_FACTORY)
	public void updateProfile(UserProfile userProfile) {

		System.out.println(ProfileConstants.UPDATE_MESSAGE_CONSUMED + userProfile);

		final UserProfile updatedUser = userProfileSerice.updateUser(userProfile);

		System.out.println(ProfileConstants.UPDATED_SUCCESSFULLY + updatedUser);

	}
	

	@KafkaListener(topics = ProfileConstants.TOPIC_DELETE_PROFILE, groupId = ProfileConstants.GROUP_JSON,
			containerFactory = ProfileConstants.LISTENER_FACTORY)
	public void deleteProfile(UserProfile userProfile) {

		System.out.println(ProfileConstants.DELETE_MESSAGE_CONSUMED + userProfile);

		userProfileSerice.deleteUser(userProfile.getUsername());

		System.out.println(ProfileConstants.RESPONSE + ProfileConstants.PROFILE_DELETED);

	}

}
