package edu.capella.ime.web.rest.resource;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.hateoas.ResourceSupport;

public class TagResource extends ResourceSupport {

    @NotNull
    @Pattern(regexp = "^[A-Z_-]*$")
    @Size(min = 1, max = 256)
	private String name;
	
    @Size(min = 1, max = 256)
	private String description;

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
}
