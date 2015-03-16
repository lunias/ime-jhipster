package edu.capella.ime.service.exception;


public class ApplicationNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ApplicationNotFoundException() {
		super();
	}
	
	public ApplicationNotFoundException(Long id) {		
		super("An application with id [" + id + "] could not be found");
	}
	
	public ApplicationNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ApplicationNotFoundException(String message) {
		super(message);
	}
	
	public ApplicationNotFoundException(Throwable cause) {
		super(cause);
	}

}
