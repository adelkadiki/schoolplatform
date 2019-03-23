package com.application.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.application.system.model.Graduate;
import com.application.system.model.Student;

public interface GraduateRepository extends CrudRepository<Graduate, Integer>{

	
	@Query("select g from Graduate g where g.name like concat('%',:name,'%')")
	List<Graduate> findByNameLike(@Param("name")String name);
	
	Graduate findById(int id);
	
	List<Graduate> findByNameContainingAndUserId(String name, int id);
	
	Graduate findGraduateByIdAndUserId(int gid, int uid);
}
