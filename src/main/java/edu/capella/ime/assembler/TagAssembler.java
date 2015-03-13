package edu.capella.ime.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import edu.capella.ime.domain.Tag;
import edu.capella.ime.web.rest.TagEndpoint;
import edu.capella.ime.web.rest.resource.TagResource;

@Component
public class TagAssembler extends ResourceAssemblerSupport<Tag, TagResource> {

	public TagAssembler() {
		super(TagEndpoint.class, TagResource.class);
	}

	@Override
	public TagResource toResource(Tag entity) {

		TagResource resource = instantiateResource(entity);
		
		BeanUtils.copyProperties(entity, resource);
		
		resource.add(linkTo(TagEndpoint.class).slash(entity.getId()).withSelfRel());	
		
		return resource;
	}

}
