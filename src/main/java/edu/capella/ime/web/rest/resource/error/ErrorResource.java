package edu.capella.ime.web.rest.resource.error;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.NativeWebRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ErrorResource extends ResourceSupport {

	private String request;
	
	private HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
	
	private String message;
	
	private Class<? extends Throwable> exception;	
	
	private List<FieldErrorResource> fieldErrors;
	
	public ErrorResource() {
		
	}
	
	public ErrorResource(HttpStatus status, Throwable exception, NativeWebRequest request) {
		
		this(status, exception.getMessage(), exception.getClass(), request);
	}
	
	public ErrorResource(HttpStatus status, String message, Class<? extends Throwable> exception, 
			NativeWebRequest request) {
		
		this.status = status;
		this.message = message;
		this.exception = exception;
		
		HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
		if (servletRequest != null) {
			this.setRequest(servletRequest.getMethod() + " " + servletRequest.getRequestURI());	
		}		
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Class<? extends Throwable> getException() {
		return exception;
	}

	public void setException(Class<? extends Throwable> exception) {
		this.exception = exception;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}
	
	public List<FieldErrorResource> getFieldErrors() {
		return fieldErrors;
	}
	
	public void setFieldErrors(List<FieldErrorResource> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}	
}
