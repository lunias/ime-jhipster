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

import edu.capella.ime.assembler.MediaAssembler;
import edu.capella.ime.assembler.TagAssembler;
import edu.capella.ime.domain.Media;
import edu.capella.ime.domain.Tag;
import edu.capella.ime.service.MediaService;
import edu.capella.ime.service.exception.InvalidResourceException;
import edu.capella.ime.web.rest.resource.MediaResource;
import edu.capella.ime.web.rest.resource.TagResource;
import edu.capella.ime.web.rest.resource.search.MediaSearchResource;

@RestController
@RequestMapping("/api/media")
@ExposesResourceFor(Media.class)
public class MediaEndpoint {

	private final Logger log = LoggerFactory.getLogger(MediaEndpoint.class);

	private MediaService mediaService;
	private MediaAssembler mediaAssembler;
	
	private TagAssembler tagAssembler;
	
	private static final String INVALID_MEDIA = "Invalid media resource";
	private static final String INVALID_MEDIA_SEARCH = "Invalid media search resource";	
	
	@Autowired
	public MediaEndpoint(MediaService mediaService, MediaAssembler mediaAssembler, 
			TagAssembler tagAssembler) {

		this.mediaService = mediaService;
		this.mediaAssembler = mediaAssembler;
		
		this.tagAssembler = tagAssembler;
	}
	
	@Timed
    @RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<PagedResources<MediaResource>> getMedia(Pageable pageable, PagedResourcesAssembler<Media> pagedAssembler) {
		
		Page<Media> page = mediaService.getMedia(pageable);
		
		PagedResources<MediaResource> pagedResources = pagedAssembler.toResource(page, mediaAssembler);
		pagedResources.add(linkTo(methodOn(this.getClass()).getAllMedia()).withRel("all"));
		pagedResources.add(linkTo(this.getClass()).slash("search").withRel("search"));		
		
		return new ResponseEntity<>(pagedResources, HttpStatus.OK);
	}	
	
	@Timed
    @RequestMapping(method = RequestMethod.GET, value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<List<MediaResource>> getAllMedia() {
		
		List<Media> media = mediaService.getMedia();		
		
		return new ResponseEntity<>(mediaAssembler.toResources(media), HttpStatus.OK);
	}
	
	@Timed
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MediaResource> getMedia(@PathVariable Long id) {
		
		Media media = mediaService.getMedia(id);
		
		return new ResponseEntity<>(mediaAssembler.toResource(media), HttpStatus.OK);
	}
	
	@Timed
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/tags", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedResources<TagResource>> getMediaTags(@PathVariable Long id, Pageable pageable, PagedResourcesAssembler<Tag> pagedAssembler) {
		
		Page<Tag> page = mediaService.getMediaTags(id, pageable);
		
		return new ResponseEntity<>(pagedAssembler.toResource(page, tagAssembler), HttpStatus.OK);
	}	
	
	@Timed
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/tags/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TagResource>> getMediaTags(@PathVariable Long id) {
		
		List<Tag> tags = mediaService.getMediaTags(id);
		
		return new ResponseEntity<>(tagAssembler.toResources(tags), HttpStatus.OK);
	}
	
	@Timed
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}/tags", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> removeMediaTags(@PathVariable Long id, @RequestBody List<Long> tagIds) {
						
		mediaService.removeTagsFromMedia(id, tagIds);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}	
	
	@Timed
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/tags", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addMediaTags(@PathVariable Long id, @RequestBody List<Long> tagIds) {
						
		mediaService.addTagsToMedia(id, tagIds);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}	
	
	@Timed
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MediaResource> updateMedia(@PathVariable Long id, @Valid @RequestBody MediaResource request, BindingResult br) {
		
		if (br.hasErrors()) {
			throw new InvalidResourceException(INVALID_MEDIA, br);
		}		
		
		Media media = mediaService.updateMedia(id, request);
		
		return new ResponseEntity<>(mediaAssembler.toResource(media), HttpStatus.OK);
	}
	
	@Timed
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteMedia(@PathVariable Long id) {
		
		mediaService.deleteMedia(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@Timed
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MediaResource> createMedia(@Valid @RequestBody MediaResource request, BindingResult br) {
		
		if (br.hasErrors()) {
			throw new InvalidResourceException(INVALID_MEDIA, br);
		}		
		
		Media media = mediaService.createMedia(request);
		
		return new ResponseEntity<>(mediaAssembler.toResource(media), HttpStatus.CREATED);
	}
	
	@Timed
	@RequestMapping(method = RequestMethod.POST, value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedResources<MediaResource>> search(@Valid @RequestBody List<MediaSearchResource> request, Pageable pageable, PagedResourcesAssembler<Media> pagedAssembler, BindingResult br) {
		
		if (br.hasErrors()) {
			throw new InvalidResourceException(INVALID_MEDIA_SEARCH, br);			
		}
		
		Page<Media> page = mediaService.searchMedia(request, pageable);
		
		PagedResources<MediaResource> pagedResources = pagedAssembler.toResource(page, mediaAssembler);		
		
		return new ResponseEntity<>(pagedResources, HttpStatus.OK);
	}
}
