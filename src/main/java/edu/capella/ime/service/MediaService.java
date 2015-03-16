package edu.capella.ime.service;

import java.util.ArrayList;
import java.util.Arrays;
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

import edu.capella.ime.domain.Media;
import edu.capella.ime.domain.Tag;
import edu.capella.ime.repository.MediaRepository;
import edu.capella.ime.service.search.SearchEnabledService;
import edu.capella.ime.web.rest.resource.MediaResource;
import edu.capella.ime.web.rest.resource.search.MediaSearchResource;

@Service
@Transactional
public class MediaService extends SearchEnabledService<Media, MediaSearchResource> {

    private final Logger log = LoggerFactory.getLogger(MediaService.class);
	
    private MediaRepository mediaRepository;
    private TagService tagService;
    
    @Autowired
    public MediaService(MediaRepository mediaRepository, TagService tagService) {
    	
    	this.mediaRepository = mediaRepository;
    	this.tagService = tagService;
    }
    
    @Transactional(readOnly = true)
    public List<Media> getMedia() {
    	
    	List<Media> media = mediaRepository.findAll();
    	
    	return media;
    }
    
    @Transactional(readOnly = true)
    public List<Media> getMedia(List<Long> mediaIds) {
    	
    	List<Media> media = mediaRepository.findAll(mediaIds);
    	
    	return media;
    }    
    
    @Transactional(readOnly = true)
    public Page<Media> getMedia(Pageable pageable) {
    	
    	Page<Media> mediaPage = mediaRepository.findAll(pageable);
    	
    	return mediaPage;
    }
    
    @Transactional(readOnly = true)
    public Media getMedia(Long mediaId) {
    	
    	Media media = mediaRepository.findOne(mediaId);
    	
    	return media;
    }
    
    @Transactional(readOnly = true)
    public List<Tag> getMediaTags(Long mediaId) {
    	
    	Media media = getMedia(mediaId);
    	media.getTags().size();
    	
    	List<Tag> tags = new ArrayList<>(media.getTags());
    	
    	return tags;
    }
    
    @Transactional(readOnly = true)
    public Page<Tag> getMediaTags(Long mediaId, Pageable pageable) {
    	
    	return tagService.getTagsForMedia(mediaId, pageable);
    }
    
    @Transactional(readOnly = true)
    public List<Media> getMediaForApplication(Long applicationId) {
    	
    	List<Media> media = mediaRepository.findByApplicationIds(Arrays.asList(applicationId));
    	
    	return media;
    }
    
    @Transactional(readOnly = true)
    public Page<Media> getMediaForApplication(Long applicationId, Pageable pageable) {    	
    	
    	Page<Media> page = mediaRepository.findByApplicationIds(Arrays.asList(applicationId), pageable);
    	
    	return page;
    }
    
    public Media updateMedia(Long mediaId, MediaResource mediaResource) {
    	
    	Media media = getMedia(mediaId);
    	
    	BeanUtils.copyProperties(mediaResource, media);
    	
    	media = mediaRepository.save(media);
    	
    	return media;
    }
    
    public Media createMedia(MediaResource mediaResource) {
    	
    	Media media = new Media(mediaResource);
    	
    	media = mediaRepository.save(media);
    	
    	return media;
    }
    
    public void deleteMedia(Long mediaId) {
    	
    	mediaRepository.delete(mediaId);
    } 
    
    public void addTagsToMedia(Long mediaId, List<Long> tagIds) {
    	
    	Media media = getMedia(mediaId);
    	
    	List<Tag> tags = tagService.getTags(tagIds);
    	
    	media.getTags().addAll(tags);
    }
    
    public void removeTagsFromMedia(Long mediaId, List<Long> tagIds) {
    	
    	Media media = getMedia(mediaId);
    	
    	List<Tag> tags = tagService.getTags(tagIds);
    	
    	media.getTags().removeAll(tags);
    }
    
    @Transactional(readOnly = true)    
    public Page<Media> searchMedia(List<MediaSearchResource> searchResources, Pageable pageable) {    	
    	
    	Page<Media> page = mediaRepository.findAll(buildSearchSpecifications(searchResources), pageable);
    	
    	return page;
    }

	@Override
	public Specification<Media> processSearchResource(MediaSearchResource searchResource) {

		if (!searchResource.getAnyOfTags().isEmpty()) {
			
			return Media.hasAnyOfTags(searchResource.getAnyOfTagsArray());
			
		} else if (!searchResource.getNoneOfTags().isEmpty()) {
			
			return Media.hasNoneOfTags(searchResource.getNoneOfTagsArray());
			
		} else if (!searchResource.getAllOfTags().isEmpty()) {
			
			return Media.hasAllOfTags(searchResource.getAllOfTagsArray());
		}		
		
		return null;
	}
}
