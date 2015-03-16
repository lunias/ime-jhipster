package edu.capella.ime.service.exception;

import org.springframework.validation.Errors;

public class InvalidResourceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private Errors errors;
	
	public InvalidResourceException(String message, Errors errors) {
		
		super(message);		
		this.errors = errors;
	}
	
	public Errors getErrors() {
		return errors;
	}
}
