package com.application.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.FirstTermResult;
import com.application.system.repository.FirstTermResultRepository;

@Service
public class FirstTermResultService {

	@Autowired
	FirstTermResultRepository repo;
	
	
	public void saveFirstTermResult(FirstTermResult result) {
		repo.save(result);
	}
}
