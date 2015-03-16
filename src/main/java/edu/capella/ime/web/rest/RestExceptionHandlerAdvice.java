package edu.capella.ime.web.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import edu.capella.ime.service.exception.ApplicationNotFoundException;
import edu.capella.ime.service.exception.InvalidResourceException;
import edu.capella.ime.service.exception.MediaNotFoundException;
import edu.capella.ime.service.exception.TagNotFoundException;
import edu.capella.ime.web.rest.resource.error.ErrorResource;
import edu.capella.ime.web.rest.resource.error.FieldErrorResource;

@ControllerAdvice
public class RestExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

	private static final HttpHeaders headers;
	
	static {
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}	
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ErrorResource error = new ErrorResource(status, ex, (NativeWebRequest) request);		
		
		return handleExceptionInternal(ex, error, headers, status, request);				
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<?> handleConstraintViolationException(DataIntegrityViolationException e, NativeWebRequest request) {
		
		HttpStatus status = HttpStatus.CONFLICT;		
		
		ErrorResource error = new ErrorResource(status, e.getRootCause(), request);
		
		return handleExceptionInternal(e, error, headers, status, request);
	}
	
	@ExceptionHandler(InvalidResourceException.class)
	protected ResponseEntity<?> handleInvalidRequestException(InvalidResourceException e, NativeWebRequest request) {
		
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		
        List<FieldErrorResource> fieldErrorResources = new ArrayList<>();

        List<FieldError> fieldErrors = e.getErrors().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
        	
            FieldErrorResource fieldErrorResource = new FieldErrorResource();
            
            fieldErrorResource.setResource(fieldError.getObjectName());
            fieldErrorResource.setField(fieldError.getField());
            fieldErrorResource.setCode(fieldError.getCode());
            fieldErrorResource.setMessage(fieldError.getDefaultMessage());
            
            fieldErrorResources.add(fieldErrorResource);
        }
        
        ErrorResource error = new ErrorResource(status, e, request);
        error.setFieldErrors(fieldErrorResources);
        
        return handleExceptionInternal(e, error, headers, status, request);
	}
	
	@ExceptionHandler(ApplicationNotFoundException.class)
	protected ResponseEntity<?> handleApplicationNotFoundException(ApplicationNotFoundException e, NativeWebRequest request) {
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		ErrorResource error = new ErrorResource(status, e, request);
		
		return handleExceptionInternal(e, error, headers, status, request);
	}
	
	@ExceptionHandler(MediaNotFoundException.class)
	protected ResponseEntity<?> handleMediaNotFoundException(MediaNotFoundException e, NativeWebRequest request) {
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		ErrorResource error = new ErrorResource(status, e, request);
		
		return handleExceptionInternal(e, error, headers, status, request);
	}
	
	@ExceptionHandler(TagNotFoundException.class)
	protected ResponseEntity<?> handleTagNotFoundException(TagNotFoundException e, NativeWebRequest request) {
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		ErrorResource error = new ErrorResource(status, e, request);
		
		return handleExceptionInternal(e, error, headers, status, request);
	}	
	
}
