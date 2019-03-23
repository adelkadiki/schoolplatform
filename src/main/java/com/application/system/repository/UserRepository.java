package com.application.system.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.application.system.model.Graduate;
import com.application.system.model.Student;
import com.application.system.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {

	User findUserByName(String name);
	
	
	@Query("select u from User u where u.name= ?1")
	User findByName(String name);
	
	User findById(int id);
	
	@Query("select u.students from User u where u.id=?1")
	List<Student> findStudent(int id);
	
	
	@Query("select u.graduates from User u where u.id=?1")
	List<Graduate> findGraduate(int id);

	
	
}
