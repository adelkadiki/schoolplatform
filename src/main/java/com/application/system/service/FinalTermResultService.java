package com.application.system.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.FinalTermResult;
import com.application.system.repository.FinalTermResultRepository;

@Service
public class FinalTermResultService {

	@Autowired
	private FinalTermResultRepository repo;
	
	public void saveFinalTermResult(FinalTermResult result) {
		repo.save(result);
	}
}
