package com.application.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.application.system.model.Student;

public interface StudentRepository extends CrudRepository<Student, Integer> {

	Student findById(int id);
	
	List<Student> findAll();
	
	
	@Query("select s from Student s where s.name like concat('%',:name,'%')")
	List<Student> findByNameLike(@Param("name")String name);
	
	@Query("select s from Student s where s.id=?1")
	List<Student> findStudentById(int id);
	
	@Query("delete Student s where s.id=?1")
	void deleteStudent(int id);
	
	
	List<Student> findByNameContainingAndUserId(String name, int id);
	
	List<Student> findStudentByUserId(int id);
	
	Student findByIdAndUserId(int sid, int uid);
	
}
