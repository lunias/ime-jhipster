package edu.capella.ime.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.capella.ime.domain.Application;
import edu.capella.ime.domain.Media;
import edu.capella.ime.repository.ApplicationRepository;
import edu.capella.ime.service.search.SearchEnabledService;
import edu.capella.ime.web.rest.resource.ApplicationResource;
import edu.capella.ime.web.rest.resource.search.ApplicationSearchResource;

@Service
@Transactional
public class ApplicationService extends SearchEnabledService<Application, ApplicationSearchResource> {

    private final Logger log = LoggerFactory.getLogger(ApplicationService.class);
    
    private ApplicationRepository applicationRepository;
    private MediaService mediaService;
    
    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, MediaService mediaService) {

    	this.applicationRepository = applicationRepository;
    	this.mediaService = mediaService;
	}
    
    @Transactional(readOnly = true)
    public List<Application> getApplications() {
    	
    	List<Application> applications = applicationRepository.findAll();
    	
    	return applications;
    }
    
    @Transactional(readOnly = true)
    public Page<Application> getApplications(Pageable pageable) {
    	
    	Page<Application> applicationPage = applicationRepository.findAll(pageable);
    	
    	return applicationPage;
    }
    
    @Transactional(readOnly = true)
    public Application getApplication(Long applicationId) {
    	
    	Application application = applicationRepository.findOne(applicationId);
    	
    	return application;
    }  
    
    @Transactional(readOnly = true)    
    public List<Media> getApplicationMedia(Long applicationId) {
    	
    	Application application = getApplication(applicationId);
    	application.getMedia().size();
    	
    	List<Media> media = new ArrayList<>(application.getMedia());
    	
    	return media;
    }
    
    public Application updateApplication(Long applicationId, ApplicationResource applicationResource) {
    	
    	Application application = getApplication(applicationId);
    	
    	BeanUtils.copyProperties(applicationResource, application);
    	
    	application = applicationRepository.save(application);
    	
    	return application;
    }
    
    public Application createApplication(ApplicationResource applicationResource) {
    	
    	Application application = new Application(applicationResource);
    	
    	application = applicationRepository.save(application);
    	
    	return application;
    }
    
    public void deleteApplication(Long applicationId) {
    	
    	applicationRepository.delete(applicationId);
    }
    
    public void addMediaToApplication(Long applicationId, List<Long> mediaIds) {
    	
    	Application application = getApplication(applicationId);
    	
    	List<Media> media = mediaService.getMedia(mediaIds);
    	
    	application.getMedia().addAll(media);
    }
    
    public void removeMediaFromApplication(Long applicationId, List<Long> mediaIds) {
    	
    	Application application = getApplication(applicationId);
    	
    	List<Media> media = mediaService.getMedia(mediaIds);
    	
    	application.getMedia().removeAll(media);
    }
    
    @Transactional(readOnly = true)    
    public Page<Application> searchApplications(List<ApplicationSearchResource> searchResources, Pageable pageable) {
    	
    	Page<Application> page = applicationRepository.findAll(buildSearchSpecifications(searchResources), pageable);
    	
    	return page;
    }

	@Override
	public Specification<Application> processSearchResource(ApplicationSearchResource searchResource) {

		if (!searchResource.getNameLike().isEmpty()) {
			
			return Application.nameLike(searchResource.getNameLike());
			
		} else if (!searchResource.getDescriptionLike().isEmpty()) {
			
			return Application.descriptionLike(searchResource.getDescriptionLike());			
		}
		
		return null;
	}    
}
