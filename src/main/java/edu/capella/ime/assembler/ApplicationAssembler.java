package edu.capella.ime.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import edu.capella.ime.domain.Application;
import edu.capella.ime.web.rest.ApplicationEndpoint;
import edu.capella.ime.web.rest.resource.ApplicationResource;

@Component
public class ApplicationAssembler extends ResourceAssemblerSupport<Application, ApplicationResource> {

	public ApplicationAssembler() {
		
		super(ApplicationEndpoint.class, ApplicationResource.class);
	}

	@Override
	public ApplicationResource toResource(Application entity) {

		ApplicationResource resource = instantiateResource(entity);
		
		BeanUtils.copyProperties(entity, resource);
		
		resource.add(linkTo(ApplicationEndpoint.class).slash(entity.getId()).withSelfRel());
		resource.add(linkTo(ApplicationEndpoint.class).slash(entity.getId()).slash("/media").withRel("media"));
		
		return resource;
	}

}
