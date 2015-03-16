package edu.capella.ime.web.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import edu.capella.ime.assembler.TagAssembler;
import edu.capella.ime.domain.Tag;
import edu.capella.ime.service.TagService;
import edu.capella.ime.service.exception.InvalidResourceException;
import edu.capella.ime.web.rest.resource.TagResource;
import edu.capella.ime.web.rest.resource.search.TagSearchResource;

@RestController
@RequestMapping("/api/tags")
@ExposesResourceFor(Tag.class)
public class TagEndpoint {

	private final Logger log = LoggerFactory.getLogger(TagEndpoint.class);
	
	private TagService tagService;
	private TagAssembler tagAssembler;
	
	private static final String INVALID_TAG = "Invalid tag resource";
	private static final String INVALID_TAG_SEARCH = "Invalid tag search resource";		
	
	@Autowired
	public TagEndpoint(TagService tagService, TagAssembler tagAssembler) {
		
		this.tagService = tagService;
		this.tagAssembler = tagAssembler;
	}
	
	@Timed
    @RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<PagedResources<TagResource>> getTags(Pageable pageable, PagedResourcesAssembler<Tag> pagedAssembler) {
		
		Page<Tag> page = tagService.getTags(pageable);
		
		PagedResources<TagResource> pagedResources = pagedAssembler.toResource(page, tagAssembler);
		pagedResources.add(linkTo(methodOn(this.getClass()).getAllTags()).withRel("all"));
		pagedResources.add(linkTo(this.getClass()).slash("search").withRel("search"));			
		
		return new ResponseEntity<>(pagedResources, HttpStatus.OK);
	}
	
	@Timed
    @RequestMapping(method = RequestMethod.GET, value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<List<TagResource>> getAllTags() {
		
		List<Tag> tags = tagService.getTags();
		
		return new ResponseEntity<>(tagAssembler.toResources(tags), HttpStatus.OK);
	}
	
	@Timed
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TagResource> getTag(@PathVariable Long id) {
		
		Tag tag = tagService.getTag(id);
		
		return new ResponseEntity<>(tagAssembler.toResource(tag), HttpStatus.OK);
	}
	
	@Timed
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TagResource> updateTag(@PathVariable Long id, @Valid @RequestBody TagResource request, BindingResult br) {
		
		if (br.hasErrors()) {
			throw new InvalidResourceException(INVALID_TAG, br);
		}		
		
		Tag tag = tagService.updateTag(id, request);		
		
		return new ResponseEntity<>(tagAssembler.toResource(tag), HttpStatus.OK);
	}
	
	@Timed
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteTag(@PathVariable Long id) {
		
		tagService.deleteTag(id);		
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}	
	
	@Timed
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TagResource> createTag(@Valid @RequestBody TagResource request, BindingResult br) {
		
		if (br.hasErrors()) {
			throw new InvalidResourceException(INVALID_TAG, br);
		}		
		
		Tag tag = tagService.createTag(request);		
		
		return new ResponseEntity<>(tagAssembler.toResource(tag), HttpStatus.CREATED);
	}
	
	@Timed
	@RequestMapping(method = RequestMethod.POST, value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedResources<TagResource>> search(@Valid @RequestBody List<TagSearchResource> request, Pageable pageable, 
			PagedResourcesAssembler<Tag> pagedAssembler, BindingResult br) {
		
		if (br.hasErrors()) {
			throw new InvalidResourceException(INVALID_TAG_SEARCH, br);			
		}		
		
		Page<Tag> page = tagService.searchTags(request, pageable);
		
		PagedResources<TagResource> tagResources = pagedAssembler.toResource(page, tagAssembler);
		
		return new ResponseEntity<>(tagResources, HttpStatus.OK);
	}
}
