package com.uxpsystems.assignment.profileservice.constant;

public class ProfileConstants {
	
	public static final String URL_ALLPROFILE = "/allprofile";
	public static final String URL_PROFILE_UN = "/profile/{userName}";
	public static final String URL_PROFILE_MOB = "/profile-from-mobile/{mobile}";
	public static final String URL_PROFILE = "/profile";
	public static final String USER_NAME = "userName";
	public static final String EXCEPTION = "Exception";
	public static final String PROFILE_NOT_CREATED = "Profile not created yet";
	public static final String MOBILE = "mobile";
	
	public static final String NOT_EXISTS = "Not Exists";
	public static final String MOBILE_NOT_EXISTS = "Mobile does not exists";
	public static final String EXISTS = "Exists";
	public static final String MOBILE_ALREADY_REGISTERED = "Mobile number alreay registered to other user";
	public static final String USER_HAVE_PROFILE = "User already have profile";
	
	public static final String SUCCESS = "Success";
	public static final String PROFILE_CREATED = "Profile created";
	public static final String TOPIC_PROFILE_JSON = "kafka_profile_json";
	public static final String GROUP_JSON = "group_json";
	public static final String LISTENER_FACTORY = "profileKafkaListenerFactory";
	public static final String UPDATE_MESSAGE_CONSUMED = "Consumed UPDATE Message: ";
	public static final String UPDATED_SUCCESSFULLY = "Profile updated successfully: ";
	
	
	public static final String TOPIC_DELETE_PROFILE = "kafka_delete_profile";
	public static final String DELETE_MESSAGE_CONSUMED = "Consumed DELETE username: ";
	public static final String RESPONSE = "Response ";
	public static final String PROFILE_DELETED = "Profile deleted";
}
