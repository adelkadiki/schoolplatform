package com.application.system.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.Student;
import com.application.system.repository.StudentRepository;


@Service
public class StudentService {

	@Autowired
	private StudentRepository repo;
	
	
	public void saveStudent(Student student) {
		repo.save(student);
	}
	
	
	public Student findStudent(int id) {
		return repo.findById(id);
	}
	
	public List<Student> findAllStudents(){
		return repo.findAll();
	}
	
	
	
	public List<Student> findByNameLike(String name){
		return repo.findByNameLike(name);
	}
	
	public List<Student> findStudentById(int id){
		return repo.findStudentById(id);
	}
	
	public void deleteStudent(int id) {
		 repo.deleteById(id);
	}
	
	public List<Student> findByStudentNameAndUserId(String name, int id){
		return repo.findByNameContainingAndUserId(name, id);
	}
	
	public List<Student> findStudensByUserId(int id){
		return repo.findStudentById(id);
	}
	
	public Student findStudentByStudentIdAndUserId(int sid, int uid) {
		return repo.findByIdAndUserId(sid, uid);
	}
}
