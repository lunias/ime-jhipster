package edu.capella.ime.service.exception;

public class TagNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public TagNotFoundException() {
		super();
	}
	
	public TagNotFoundException(Long id) {		
		super("A tag with id [" + id + "] could not be found");
	}
	
	public TagNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TagNotFoundException(String message) {
		super(message);
	}
	
	public TagNotFoundException(Throwable cause) {
		super(cause);
	}
}
