package com.application.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.SixthClass;
import com.application.system.repository.SxithClassRepository;

@Service
public class SixthClassService {
	
	@Autowired
	SxithClassRepository repo;
	
	
	public void saveSixthClass(SixthClass sixthclass) {
		
		repo.save(sixthclass);
	}

}
