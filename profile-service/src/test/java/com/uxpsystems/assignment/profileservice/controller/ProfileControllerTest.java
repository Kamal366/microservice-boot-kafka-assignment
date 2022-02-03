package com.uxpsystems.assignment.profileservice.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uxpsystems.assignment.profileservice.constant.ProfileConstants;
import com.uxpsystems.assignment.profileservice.model.UserProfile;
import com.uxpsystems.assignment.profileservice.service.UserProfileService;

//comment added
@RunWith(SpringRunner.class)
@WebMvcTest(value = ProfileController.class)
public class ProfileControllerTest {
	
	
	/*
	 * @Autowired private MockMvc mockMvc;
	 */
	
	@Autowired
	  private WebApplicationContext webApplicationContext;
	  private MockMvc mockMvc;

	  @BeforeEach
	  public void setUp() {
	    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	  }

	@MockBean
	private UserProfileService profileService;
	
	private static final UserProfile mockProfile=new UserProfile("admin","bbsr","8595645865");;
	
	
	@Test
	public void testCreateProfile() throws Exception {
		
        String inputInJson = this.mapToJson(mockProfile);
		
		String URI = "/api/user/profile";
		
		Mockito.when(profileService.save(Mockito.any(UserProfile.class))).thenReturn(mockProfile);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(URI)
				.accept(MediaType.APPLICATION_JSON).content(inputInJson)
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		String outputInJson = response.getContentAsString();
		String expectedJson = this.mapToJson(new JSONObject().put(ProfileConstants.SUCCESS,ProfileConstants.PROFILE_CREATED).toMap());
		
		assertThat(outputInJson).isEqualTo(expectedJson);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
	}
	
	@Test
	public void testGetUserById() throws Exception {
	
		String inputInJson = this.mapToJson(mockProfile);
		String URI = "/api/user/profile/admin";
		Mockito.when(profileService.findByUsername(Mockito.anyString())).thenReturn(mockProfile);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(URI)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
		
		String outputInJson = response.getContentAsString();
		assertThat(outputInJson).isEqualTo(inputInJson);
		assertEquals(HttpStatus.OK.value(), response.getStatus());

	}
	

	@Test
	public void testGetUserByMobileIfExists() throws Exception {
		
		String URI = "/api/user/profile-from-mobile/8595645865";
		Mockito.when(profileService.findUserByMobile(Mockito.anyString())).thenReturn(mockProfile);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(URI)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
		
		String outputInJson = result.getResponse().getContentAsString();
		String expectedInJson=this.mapToJson(new JSONObject().put(ProfileConstants.EXISTS,ProfileConstants.MOBILE_ALREADY_REGISTERED).toMap());
		assertThat(outputInJson).isEqualTo(expectedInJson);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
	}
	
	
	@Test
	public void testGetUserByMobileIfNotExists() throws Exception {
	
		UserProfile mockProfile = null;
		
		String URI = "/api/user/profile-from-mobile/8595645862";
		Mockito.when(profileService.findUserByMobile(Mockito.anyString())).thenReturn(mockProfile);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(URI)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
		
		String outputInJson = result.getResponse().getContentAsString();
		String expectedInJson=this.mapToJson(new JSONObject().put(ProfileConstants.NOT_EXISTS,ProfileConstants.MOBILE_NOT_EXISTS).toMap());
		assertThat(outputInJson).isEqualTo(expectedInJson);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
	}
	
	@Test
	public void testGetAllUsers() throws Exception {
	
		UserProfile mockProfile1 = new UserProfile("admin","bbsr","8595645865");
		UserProfile mockProfile2 = new UserProfile("kamal","delhi","7845956235");
		
		
		List<UserProfile> profileList = new ArrayList<>();
		profileList.add(mockProfile1);
		profileList.add(mockProfile2);
		
		String URI = "/api/user/allprofile";
		Mockito.when(profileService.findAll()).thenReturn(profileList);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get(URI)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		String expectedJson = this.mapToJson(profileList);
		String outputInJson = result.getResponse().getContentAsString();
		assertThat(outputInJson).isEqualTo(expectedJson);

	}
	

	/**
	 * Maps an Object into a JSON String. Uses a Jackson ObjectMapper.
	 */
	private String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}
     
}
