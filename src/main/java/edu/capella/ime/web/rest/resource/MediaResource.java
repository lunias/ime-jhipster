package edu.capella.ime.web.rest.resource;

import org.springframework.hateoas.ResourceSupport;

public class MediaResource extends ResourceSupport {

	private String mediaValue;

	public String getMediaValue() {
		return mediaValue;
	}

	public void setMediaValue(String mediaValue) {
		this.mediaValue = mediaValue;
	}	
	
}
