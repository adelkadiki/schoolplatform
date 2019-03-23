package com.application.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.NinthClass;
import com.application.system.repository.NinthClassRepository;

@Service
public class NinthClassService {
	
	@Autowired
	private NinthClassRepository repo;
	
	
	public void saveNinthClass(NinthClass ninthclass) {
		
		repo.save(ninthclass);
		
	}

}
