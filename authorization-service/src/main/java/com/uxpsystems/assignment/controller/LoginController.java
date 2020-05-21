package com.uxpsystems.assignment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uxpsystems.assignment.config.TokenUtil;
import com.uxpsystems.assignment.dao.User;
import com.uxpsystems.assignment.model.TokenRequest;
import com.uxpsystems.assignment.model.TokenResponse;
import com.uxpsystems.assignment.model.UserDTO;
import com.uxpsystems.assignment.model.UserProfile;
import com.uxpsystems.assignment.service.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
public class LoginController {


	private static final String BEARER = "Bearer ";

	private static final String LOGIN = "/login";

	private static final String REGISTER = "/register";

	private static final String USER_DISABLED = "User Disabled";

	private static final String INVALID_CREDENTIAL = "Invalid Credential";

	private static final String PROFILE_DELETED = "Profile deleted";

	private static final String PROFILE_UPDATED = "Profile updated";

	private static final String RESPONSE = "Response";

	private static final String SUCCESS = "success";

	private static final String PROFILE = "/profile";

	private static final String ALLUSERS = "/allusers";

	private static final String PROBLEM_IN_CREATING_PROFILE = "Problem in creating profile";

	private static final String FAILED = "Failed";
	
	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenUtil tokenUtil;

	/**
	 * @param authenticationRequest
	 * @return ResponseEntity User logged in
	 * @throws Exception
	 */
	@RequestMapping(value = LOGIN, method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody TokenRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = tokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new TokenResponse(BEARER+token));
	}
	
	/**
	 * @param user
	 * @return ResponseEntity registerd user data
	 * @throws Exception
	 */
	@RequestMapping(value = REGISTER, method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
		return ResponseEntity.ok(userService.save(user));
	}

	/**
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception(USER_DISABLED, e);
		} catch (BadCredentialsException e) {
			throw new Exception(INVALID_CREDENTIAL, e);
		}
	}
	
	/**
	 * @return all user data
	 */
	@GetMapping(ALLUSERS) public List<User> getAllUsers() {
		return userService.findAll(); 
	}

	/**
	 * @param request
	 * @return logged in user profile
	 */
	@GetMapping(value=PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getUserProfile(HttpServletRequest request){

		String response = userService.getProfile(request);
				
		return ResponseEntity.ok().body(response);

	}

	/**
	 * @param request
	 * @param userDetails
	 * @return create user response
	 * @throws ParseException
	 */
	@PostMapping(value=PROFILE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createUserProfile(HttpServletRequest request, @Valid @RequestBody (required = true) UserProfile userDetails) throws ParseException{

		String result = userService.createProfile(request,userDetails);
		
        if(result==null){
			
			return new ResponseEntity<>(new org.json.JSONObject().put(FAILED,PROBLEM_IN_CREATING_PROFILE).toMap(), HttpStatus.OK);
		}
				
        return ResponseEntity.ok().body(result);
		
	}

	/**
	 * @param request
	 * @param userDetails
	 * @return update user profile message response
	 * @throws ParseException
	 */
	@PutMapping(value=PROFILE,  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateUserProfile(HttpServletRequest request,@Valid @RequestBody (required = true) UserProfile userDetails) throws ParseException{

		String result = userService.updateProfile(request,userDetails);
		
       if(!result.equalsIgnoreCase(SUCCESS)){
			
			return new ResponseEntity<>(new org.json.JSONObject().put(FAILED,result).toMap(), HttpStatus.OK);
		}

		return new ResponseEntity<>(new org.json.JSONObject().put(RESPONSE,PROFILE_UPDATED).toMap(), HttpStatus.OK);

	}



	/**
	 * @param request
	 * @return delete user profile message response
	 * @throws ParseException
	 */
	@DeleteMapping(value=PROFILE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteUser(HttpServletRequest request) throws ParseException {

		String result = userService.deleteProfile(request);
		
		if(!result.equalsIgnoreCase(SUCCESS)){
			
			return new ResponseEntity<>(new org.json.JSONObject().put(FAILED,result).toMap(), HttpStatus.OK);
		}

		return new ResponseEntity<>(new org.json.JSONObject().put(RESPONSE,PROFILE_DELETED).toMap(), HttpStatus.OK);
	}

}
