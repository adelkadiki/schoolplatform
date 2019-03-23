package com.application.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.FirstTerm;
import com.application.system.repository.FirstTermRepository;

@Service
public class FirstTermService {

	@Autowired
	private FirstTermRepository repo;
	
	public void saveFirstTerm(FirstTerm term) {
		repo.save(term);
	}
	
	
	public void delateFirstTerm(FirstTerm term) {
		repo.delete(term);
	}
}
