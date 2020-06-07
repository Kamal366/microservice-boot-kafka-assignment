package com.uxpsystems.assignment.profileservice.dao;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.context.junit4.SpringRunner;

import com.uxpsystems.assignment.profileservice.model.UserProfile;
import com.uxpsystems.assignment.profileservice.repository.UserProfileRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserProfileDaoTest {
	
	
	@Autowired
	private UserProfileRepository userProfileRepository;
	
	private static final UserProfile profile = new UserProfile("steve", "us", "8541268695");
	
	@Test
	public void testSaveProfile(){
		
		UserProfile savedInDb=userProfileRepository.save(profile);
		UserProfile getFromDb= userProfileRepository.findByUsername(savedInDb.getUsername());
		assertNotNull(profile);
        assertEquals(getFromDb.getUsername(), savedInDb.getUsername());
		
		
	}
	
	@Test
	public void testGetProfileByUsername(){
		
		UserProfile savedInDb=userProfileRepository.save(profile);
		UserProfile getFromDb= userProfileRepository.findByUsername(savedInDb.getUsername());
		assertThat(getFromDb).isEqualTo(savedInDb);
		
	}
	
	@Test
	public void testGetProfileByMobile(){
		
		UserProfile savedInDb=userProfileRepository.save(profile);
		UserProfile getFromDb= userProfileRepository.findByMobile(savedInDb.getMobile());
		assertThat(getFromDb).isEqualTo(savedInDb);
		
	}
	
	@Test
	public void testGetAllProfile(){
		
		UserProfile profile = new UserProfile("John", "us", "8523125635"); 
		
		userProfileRepository.save(profile);
		assertNotNull(userProfileRepository.findAll());
		
	}
	
	@Test
	public void testUpdateProfile(){
		
		UserProfile profile = new UserProfile("John", "us", "8547125635"); 
		profile.setAddress("france");
		UserProfile savedInDb=userProfileRepository.save(profile);
		UserProfile getFromDb= userProfileRepository.findByUsername(savedInDb.getUsername());
        assertEquals(getFromDb.getAddress(), profile.getAddress());
		
	}
	
	@Test
	public void testDeleteProfile(){
		
		UserProfile profile = new UserProfile("Stefan", "us", "8547125225"); 
		
		userProfileRepository.save(profile);
		userProfileRepository.delete(profile);
		assertNull(userProfileRepository.findByUsername(profile.getUsername()));
		
	}
	
	

}
