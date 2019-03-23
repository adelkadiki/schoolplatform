package com.application.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.FourthClass;
import com.application.system.repository.FourthClassRepository;

@Service
public class FourthClassService {

	@Autowired
	FourthClassRepository repo;
	
	public void saveFourthClass(FourthClass fourthclass) {
		repo.save(fourthclass);
	}
}
