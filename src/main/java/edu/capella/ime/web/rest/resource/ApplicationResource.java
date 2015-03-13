package edu.capella.ime.web.rest.resource;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.hateoas.ResourceSupport;

import edu.capella.ime.domain.ApplicationStatus;

public class ApplicationResource extends ResourceSupport {

    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9 ]*$")
    @Size(min = 1, max = 256)
	private String name;
	
    @Size(min = 1, max = 256)
	private String description;
	
    @NotNull
	private ApplicationStatus status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}       
}
