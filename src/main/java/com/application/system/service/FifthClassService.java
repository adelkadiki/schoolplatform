package com.application.system.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.FifthClass;
import com.application.system.repository.FifthClassRepository;

@Service
public class FifthClassService {

	@Autowired
	FifthClassRepository repo;
	
	public void saveFifthClass(FifthClass fifthclass) {
		repo.save(fifthclass);
	}
}
