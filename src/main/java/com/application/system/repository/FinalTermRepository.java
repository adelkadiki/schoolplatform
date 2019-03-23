package com.application.system.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.application.system.model.FinalTerm;

public interface FinalTermRepository extends CrudRepository<FinalTerm, Integer> {

	
}
