package  com.uxpsystems.assignment.profileservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import  com.uxpsystems.assignment.profileservice.model.UserProfile;


@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{

	UserProfile findByUsername(String uname);
	UserProfile findByMobile(String mobile);

}
