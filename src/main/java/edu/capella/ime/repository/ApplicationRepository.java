package edu.capella.ime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.capella.ime.domain.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
	
}
