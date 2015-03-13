package edu.capella.ime.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import edu.capella.ime.domain.Media;
import edu.capella.ime.web.rest.MediaEndpoint;
import edu.capella.ime.web.rest.resource.MediaResource;

@Component
public class MediaAssembler extends ResourceAssemblerSupport<Media, MediaResource> {

	public MediaAssembler() {
		super(MediaEndpoint.class, MediaResource.class);
	}

	@Override
	public MediaResource toResource(Media entity) {

		MediaResource resource = instantiateResource(entity);

		BeanUtils.copyProperties(entity, resource);
		
		resource.add(linkTo(MediaEndpoint.class).slash(entity.getId()).withSelfRel());
		resource.add(linkTo(MediaEndpoint.class).slash(entity.getId()).slash("tags").withRel("tags"));		
		
		return resource;
	}

}
