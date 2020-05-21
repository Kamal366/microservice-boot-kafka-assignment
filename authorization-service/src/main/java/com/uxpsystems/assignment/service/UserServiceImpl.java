package com.uxpsystems.assignment.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

import com.uxpsystems.assignment.dao.User;
import com.uxpsystems.assignment.model.UserDTO;
import com.uxpsystems.assignment.model.UserProfile;
import com.uxpsystems.assignment.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService,UserDetailsService{

	private static final String USER_NOT_FOUND_WITH_USERNAME = "User not found with username: ";

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * @return
	 */
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	@Value("${profile.server.api.url}")
	private String profileServerUrl;
	
	@Value("${profile.server.api.url.mobile}")
	private String profileServerUrlMobile;

	private static final String TOPIC = "kafka_profile_json";

	private static final String TOPIC_delete = "kafka_delete_profile";

	private static final String pd_regex = "^[6-9]\\d{9}$";

	private static final String address_regex ="^[A-Za-z0-9]{4,}$";
	
	private static final String EXISTS = "Exists";

	private static final String SUCCESS = "success";

	private static final String NO_PROFILE_FOUND_FOR_THIS_USER = "No profile found for this user";

	private static final String INVALID_MOBILE_NUMBER = "Invalid mobile number";

	private static final String MOBILE_NOT_FOUND = "mobile not found in request body";

	private static final String INVALID_ADDRESS = "Invalid address.(Hint: Should not contain any special character and minimum 4 character/number required)";

	private static final String ADDRESS_NOT_FOUND = "address not found in request body";

	private static final String USERNAME_NOT_REQUIRED = "username not required, as the user doesn't have have access to other user's profile";

	private static final String UPDATE = "update";

	private static final String MOBILE = "mobile";

	public static final String VALID = "valid";

	private static final String USERNAME = "username";

	private static final String ADDRESS = "address";
	
	@Autowired
	private PasswordEncoder bcryptEncoder;

	
	/**
	 * fin user by username
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(USER_NOT_FOUND_WITH_USERNAME + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}
	
	/**
	 * Save User Auth data
	 */
	public User save(UserDTO user) {
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return userRepository.save(newUser);
	}

	/**
	 * get all user profile data
	 */
	@Override
	public List<User> findAll() {
		return userRepository.findAll(); 
	}

	/**
	 *find profile by username
	 */
	@Override
	public User findByUsername(String userName) {
		return userRepository.findByUsername(userName);
	}

	/**
	 *Get Profile data method
	 */
	@Override
	public String getProfile(HttpServletRequest request) {

		Principal userPrincipal =  request.getUserPrincipal();
		String userName= userPrincipal.getName();

		final String uri = profileServerUrl+"/"+userName;

		String response = restTemplate.exchange(uri,HttpMethod.GET,null, String.class).getBody();

		return response;

	}

	/**
	 *Create Profile method
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String createProfile(HttpServletRequest request, @Valid UserProfile userDetails) throws ParseException {


		Principal userPrincipal =  request.getUserPrincipal();
		String userName= userPrincipal.getName();

		String message=validateProfileData(userDetails, request, "create");

		if(!message.equalsIgnoreCase(VALID)) {

			return message;

		}

		final String uri = profileServerUrl;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject userJsonObject = new JSONObject();
		userJsonObject.put(USERNAME,userName);
		userJsonObject.put(ADDRESS,userDetails.getAddress());
		userJsonObject.put(MOBILE, userDetails.getMobile());

		HttpEntity<String> req = 
				new HttpEntity<String>(userJsonObject.toString(), headers);

		String result = restTemplate.exchange(uri,HttpMethod.POST,req, String.class).getBody();
		
		return result.replace("\\", "");
	}


	/**
	 *Update Profile data
	 */
	@Override
	public String updateProfile(HttpServletRequest request, @Valid UserProfile userDetails) throws ParseException {
		Principal userPrincipal =  request.getUserPrincipal();
		String userName= userPrincipal.getName();

		String message=validateProfileData(userDetails, request, UPDATE);

		if(!message.equalsIgnoreCase(VALID)) {

			return message;

		}

		userDetails.setUsername(userName);
		kafkaTemplate.send(TOPIC, userDetails);

		return SUCCESS;
	}

	/**
	 *Delete logged user profile
	 */
	@Override
	public String deleteProfile(HttpServletRequest request) throws ParseException {
		final String userProfileExists =getProfile(request);

		JSONParser parser = new JSONParser(); 
		JSONObject json = (JSONObject) parser.parse(userProfileExists);

		if(json.isEmpty() || !json.containsKey(USERNAME)) {

			return NO_PROFILE_FOUND_FOR_THIS_USER;
		}

		kafkaTemplate.send(TOPIC_delete, json);

		return SUCCESS;
	}

	/**
	 * @param userDetails
	 * @param request
	 * @param operation
	 * @return String validate data
	 * @throws ParseException
	 */
	private String validateProfileData(@Valid UserProfile userDetails,HttpServletRequest request,String operation) throws ParseException {


		if(userDetails.getUsername()!=null) {

			return USERNAME_NOT_REQUIRED;

		}else if(userDetails.getAddress()==null) {

			return ADDRESS_NOT_FOUND;

		}else if(!userDetails.getAddress().matches(address_regex)) {

			return INVALID_ADDRESS;

		}else if(userDetails.getMobile()==null) {

			return MOBILE_NOT_FOUND;

		}else if(!userDetails.getMobile().matches(pd_regex)) {

			return INVALID_MOBILE_NUMBER;

		}

		if(operation.equalsIgnoreCase(UPDATE)) {

			final String userProfileExists =getProfile(request);

			JSONParser parser = new JSONParser(); 
			JSONObject json = (JSONObject) parser.parse(userProfileExists);

			if(json.isEmpty() || !json.containsKey(USERNAME)) {

				return NO_PROFILE_FOUND_FOR_THIS_USER;

			}else if(!json.get(MOBILE).equals(userDetails.getMobile())){

				final String uri = profileServerUrlMobile+userDetails.getMobile();

				String result = restTemplate.exchange(uri,HttpMethod.GET,null, String.class).getBody();

				parser = new JSONParser(); 
				json = (JSONObject) parser.parse(result);

				if (json.containsKey(EXISTS)) {
					return json.get(EXISTS).toString();
				}
			}

		}

		return VALID;

	}
	
}
