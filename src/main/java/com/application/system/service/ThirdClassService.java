package com.application.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.ThirdClass;
import com.application.system.repository.ThirdClassRepository;

@Service
public class ThirdClassService {
	
	@Autowired
	ThirdClassRepository repo;
	
	public void saveThirdClass(ThirdClass thirdclass) {
		
		repo.save(thirdclass);
	}

}
