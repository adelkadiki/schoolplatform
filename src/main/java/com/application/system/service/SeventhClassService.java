package com.application.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.SeventhClass;
import com.application.system.repository.SeventhClassRepository;

@Service
public class SeventhClassService {

	@Autowired
	private SeventhClassRepository repo;
	
	
	public void saveSeventhClass(SeventhClass seventhclass) {
		repo.save(seventhclass);
	}
}
