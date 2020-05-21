package com.uxpsystems.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uxpsystems.assignment.dao.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByUsername(String uname);

}
