package com.uxpsystems.assignment.profileservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.uxpsystems.assignment.profileservice.model.UserProfile;
import com.uxpsystems.assignment.profileservice.repository.UserProfileRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProfileServiceTest {
	
	@Autowired
	private UserProfileService userProfileService;
	
	@MockBean
	private UserProfileRepository userProfileRepository;
	
	private static final UserProfile profile = new UserProfile("kamal", "delhi", "8956412374");
	
	@Test
	public void testCreateProfile(){

	    Mockito.when(userProfileRepository.save(profile)).thenReturn(profile);
	    assertThat(userProfileService.save(profile)).isEqualTo(profile);
	
	}
	
	@Test
	public void testGetUserById(){
		
	    Mockito.when(userProfileRepository.findByUsername("kamal")).thenReturn(profile);
	    assertThat(userProfileService.findByUsername("kamal")).isEqualTo(profile);
	}
	
	@Test
	public void testGetUserByMobile(){
		
	    Mockito.when(userProfileRepository.findByMobile("8956412374")).thenReturn(profile);
	    assertThat(userProfileService.findUserByMobile("8956412374")).isEqualTo(profile);
	}
	
	@Test
	public void testGetAllProfile(){
		
		UserProfile profile1 = new UserProfile("admin","bbsr","8595645865");
		UserProfile profile2 = new UserProfile("kamal", "delhi", "8956412374");
		
		List<UserProfile> profileList = new ArrayList<>();
		profileList.add(profile1);
		profileList.add(profile2);
		
	    Mockito.when(userProfileRepository.findAll()).thenReturn(profileList);
	    assertThat(userProfileService.findAll()).isEqualTo(profileList);
	}
	
	@Test
	public void testUpdateProfile(){
		
        Mockito.when(userProfileRepository.findByUsername("kamal")).thenReturn(profile);
		
        profile.setAddress("pune");
        profile.setMobile("8855445522");
		Mockito.when(userProfileRepository.save(profile)).thenReturn(profile);
		
		assertThat(userProfileService.updateUser(profile)).isEqualTo(profile);
	}
	
	@Test
	public void testDeleteProfile(){
		
		 Mockito.when(userProfileRepository.findByUsername("kamal")).thenReturn(profile);
		 //Mockito.when(userProfileRepository.exists(profile.getUsername())).thenReturn(false);
		 userProfileService.deleteUser(profile.getUsername());
		 verify(userProfileRepository, times(1)).delete(profile);
	}
	

}
