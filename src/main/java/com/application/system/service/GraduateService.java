package com.application.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.Graduate;
import com.application.system.repository.GraduateRepository;

@Service
public class GraduateService {

	@Autowired
	private GraduateRepository repo;
	
	public void saveGraduate(Graduate graduate) {
		repo.save(graduate);
	}
	
	public List<Graduate> findGraduates(String name){
		
		return repo.findByNameLike(name);
	}
	
	public Graduate findById(int id) {
		return repo.findById(id);
	}
	
	public List<Graduate> findGraduateByNameLikeAndUserId(String name, int id){
		return repo.findByNameContainingAndUserId(name, id);
	}
	
	public Graduate findGraduateByIdAndUserId(int gid, int uid) {
		return repo.findGraduateByIdAndUserId(gid, uid);
	}
}
