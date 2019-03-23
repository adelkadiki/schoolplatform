package com.application.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.system.model.Info;
import com.application.system.repository.InfoRepository;

@Service
public class InfoService {
	
	@Autowired
	private InfoRepository repo;

	public void saveInfo(Info info) {
		repo.save(info);
	}
	
	public void deleteInfo(int id) {
		repo.deleteById(id);
	}
}
