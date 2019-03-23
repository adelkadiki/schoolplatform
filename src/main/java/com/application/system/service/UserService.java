package com.application.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.User;
import com.application.system.model.Graduate;
import com.application.system.model.Student;
import com.application.system.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repo;
	
	public User findUserByName(String name) {
		return repo.findUserByName(name);
	}
	
	
	public void saveUser(User user) {
		repo.save(user);
	}
	
	public User findByName(String name) {
		return repo.findByName(name);
	}
	
	public User findById(int id) {
		return repo.findById(id);
	}

	public List<Student> findStudents(int id){
		return repo.findStudent(id);
	}
	
	
	public List<Graduate> findGraudate(int id){
		return repo.findGraduate(id);
	}
	
	
	
}
