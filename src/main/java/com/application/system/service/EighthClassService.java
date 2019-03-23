package com.application.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.EighthClass;
import com.application.system.repository.EighthClassRepository;

@Service
public class EighthClassService {
	
	@Autowired
	private EighthClassRepository repo;

	
	public void saveEighthClass(EighthClass eighthclass) {
	
		repo.save(eighthclass);
	}
}
