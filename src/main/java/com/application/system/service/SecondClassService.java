package com.application.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.SecondClass;
import com.application.system.repository.SecondClassRepository;

@Service
public class SecondClassService {
	
	@Autowired
	SecondClassRepository repo;
	
	public void saveSecondClass(SecondClass secondclass) {
		repo.save(secondclass);
	}

}
