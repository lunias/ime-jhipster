package edu.capella.ime.service.exception;

public class MediaNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public MediaNotFoundException() {
		super();
	}
	
	public MediaNotFoundException(Long id) {		
		super("Media with id [" + id + "] could not be found");
	}
	
	public MediaNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public MediaNotFoundException(String message) {
		super(message);
	}
	
	public MediaNotFoundException(Throwable cause) {
		super(cause);
	}
}
