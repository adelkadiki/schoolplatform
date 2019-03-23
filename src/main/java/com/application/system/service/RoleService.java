package com.application.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.application.system.model.Role;
import com.application.system.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository repo;
	
	public void saveRole(Role role) {
		repo.save(role);
	}
	
}
