package edu.capella.ime.web.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import edu.capella.ime.assembler.ApplicationAssembler;
import edu.capella.ime.assembler.MediaAssembler;
import edu.capella.ime.domain.Application;
import edu.capella.ime.domain.Media;
import edu.capella.ime.service.ApplicationService;
import edu.capella.ime.service.MediaService;
import edu.capella.ime.web.rest.resource.ApplicationResource;
import edu.capella.ime.web.rest.resource.MediaResource;
import edu.capella.ime.web.rest.resource.search.ApplicationSearchResource;

@RestController
@RequestMapping("/api/applications")
@ExposesResourceFor(Application.class)
public class ApplicationEndpoint {
	
	private final Logger log = LoggerFactory.getLogger(ApplicationEndpoint.class);
	
	private ApplicationService applicationService;	
	private ApplicationAssembler applicationAssembler;
	
	private MediaService mediaService;
	private MediaAssembler mediaAssembler;

	@Autowired
	public ApplicationEndpoint(ApplicationService applicationService, ApplicationAssembler applicationAssembler,
			MediaService mediaService, MediaAssembler mediaAssembler) {
		
		this.applicationService = applicationService;
		this.applicationAssembler = applicationAssembler;
		
		this.mediaService = mediaService;
		this.mediaAssembler = mediaAssembler;
	}
	
	@Timed
    @RequestMapping(method = RequestMethod.GET, value = "", produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<PagedResources<ApplicationResource>> getApplications(Pageable pageable, PagedResourcesAssembler<Application> pagedAssembler) {
		
		Page<Application> page = applicationService.getApplications(pageable);
		
		PagedResources<ApplicationResource> pagedResources = pagedAssembler.toResource(page, applicationAssembler);
		pagedResources.add(linkTo(methodOn(this.getClass()).getAllApplications()).withRel("all"));
		pagedResources.add(linkTo(this.getClass()).slash("search").withRel("search"));	
		
		return new ResponseEntity<>(pagedResources, HttpStatus.OK);
	}	
	
	@Timed
    @RequestMapping(method = RequestMethod.GET, value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<List<ApplicationResource>> getAllApplications() {
		
		List<Application> applications = applicationService.getApplications();
		
		return new ResponseEntity<>(applicationAssembler.toResources(applications), HttpStatus.OK);
	}
	
	@Timed
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApplicationResource> getApplication(@PathVariable Long id) {
		
		Application application = applicationService.getApplication(id);
		
		return new ResponseEntity<>(applicationAssembler.toResource(application), HttpStatus.OK);
	}
	
	@Timed
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/media", produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<PagedResources<MediaResource>> getApplicationMedia(@PathVariable Long id, Pageable pageable, PagedResourcesAssembler<Media> pagedAssembler) {
		
		Page<Media> page = mediaService.getMediaForApplication(id, pageable);
		
		PagedResources<MediaResource> pagedResources = pagedAssembler.toResource(page, mediaAssembler);
		pagedResources.add(linkTo(methodOn(this.getClass()).getAllApplicationMedia(id)).withRel("all"));		
		
		return new ResponseEntity<>(pagedResources, HttpStatus.OK);
	}	
	
	@Timed
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/media/all", produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<List<MediaResource>> getAllApplicationMedia(@PathVariable Long id) {
		
		List<Media> media = applicationService.getApplicationMedia(id);
		
		return new ResponseEntity<>(mediaAssembler.toResources(media), HttpStatus.OK);
	}
	
	@Timed
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}/media", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> removeApplicationMedia(@PathVariable Long id, @RequestBody List<Long> mediaIds) {
						
		applicationService.removeMediaFromApplication(id, mediaIds);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@Timed
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/media", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addApplicationMedia(@PathVariable Long id, @RequestBody List<Long> mediaIds) {
						
		applicationService.addMediaToApplication(id, mediaIds);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}	
	
	@Timed
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApplicationResource> updateApplication(@PathVariable Long id, @RequestBody ApplicationResource request) {
		
		Application application = applicationService.updateApplication(id, request);
		
		return new ResponseEntity<>(applicationAssembler.toResource(application), HttpStatus.OK);
	}
	
	@Timed
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteApplication(@PathVariable Long id) {
		
		applicationService.deleteApplication(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@Timed
	@RequestMapping(method = RequestMethod.POST, value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApplicationResource> createApplication(@RequestBody ApplicationResource request) {
		
		Application application = applicationService.createApplication(request);
		
		return new ResponseEntity<>(applicationAssembler.toResource(application), HttpStatus.CREATED);
	}
	
	@Timed
	@RequestMapping(method = RequestMethod.POST, value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<PagedResources<ApplicationResource>> search(@RequestBody List<ApplicationSearchResource> request, Pageable pageable, PagedResourcesAssembler<Application> pagedAssembler) {
		
		Page<Application> page = applicationService.searchApplications(request, pageable);
		
		PagedResources<ApplicationResource> pagedResources = pagedAssembler.toResource(page, applicationAssembler);
		
		return new ResponseEntity<>(pagedResources, HttpStatus.OK);
	}
}
