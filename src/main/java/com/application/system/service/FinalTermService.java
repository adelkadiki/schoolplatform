package com.application.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.FinalTerm;
import com.application.system.repository.FinalTermRepository;

@Service
public class FinalTermService {

	@Autowired
	private FinalTermRepository repo;
	
	
	public void saveFianlTerm(FinalTerm term) {
		repo.save(term);
	}
	
	public void deleteFinalTerm(FinalTerm term) {
		repo.delete(term);
	}
	
}
