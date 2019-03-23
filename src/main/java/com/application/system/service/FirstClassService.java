package com.application.system.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.FirstClass;
import com.application.system.repository.FirstClassRepository;

@Service
public class FirstClassService {

	@Autowired
	private FirstClassRepository repo;
	
	public void saveFirstClass(FirstClass firstclass) {
		repo.save(firstclass);
	}
}
