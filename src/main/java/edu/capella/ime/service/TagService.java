package edu.capella.ime.service;

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

import edu.capella.ime.domain.Tag;
import edu.capella.ime.repository.MediaRepository;
import edu.capella.ime.repository.TagRepository;
import edu.capella.ime.service.exception.MediaNotFoundException;
import edu.capella.ime.service.exception.TagNotFoundException;
import edu.capella.ime.service.search.SearchEnabledService;
import edu.capella.ime.web.rest.resource.TagResource;
import edu.capella.ime.web.rest.resource.search.TagSearchResource;

@Service	
@Transactional
public class TagService extends SearchEnabledService<Tag, TagSearchResource> {
	
    private final Logger log = LoggerFactory.getLogger(TagService.class);
    
    private TagRepository tagRepository;    
    private MediaRepository mediaRepository;
    
    @Autowired
    public TagService(TagRepository tagRepository, MediaRepository mediaRepository) {
    	
    	this.tagRepository = tagRepository;
    	this.mediaRepository = mediaRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Tag> getTags() {
    	
    	List<Tag> tags = tagRepository.findAll();
    	
    	return tags;
    }
    
    @Transactional(readOnly = true)
    public List<Tag> getTags(List<Long> tagIds) {
    	
    	List<Tag> tags = tagRepository.findAll(tagIds);
    	
    	return tags;
    }
    
    @Transactional(readOnly = true)
    public Page<Tag> getTags(Pageable pageable) {
    	
    	Page<Tag> tagPage = tagRepository.findAll(pageable);
    	
    	return tagPage;
    }

    @Transactional(readOnly = true)
    public Tag getTag(Long tagId) throws TagNotFoundException {
    	
    	Tag tag = tagRepository.findOne(tagId);
    	
    	if (tag == null) throw new TagNotFoundException(tagId);
    	
    	return tag;
    }
    
    @Transactional(readOnly = true)
    public List<Tag> getTagsForMedia(Long mediaId) throws MediaNotFoundException {
    	
    	if (!mediaRepository.exists(mediaId)) throw new MediaNotFoundException(mediaId);
    	
    	List<Tag> tags = tagRepository.findByMediaIds(Arrays.asList(mediaId));
    	
    	return tags;
    }
    
    @Transactional(readOnly = true)
    public Page<Tag> getTagsForMedia(Long mediaId, Pageable pageable) throws MediaNotFoundException {
    	
    	if (!mediaRepository.exists(mediaId)) throw new MediaNotFoundException(mediaId);    	
    	
    	Page<Tag> page = tagRepository.findByMediaIds(Arrays.asList(mediaId), pageable);
    	
    	return page;
    }
    
    public Tag updateTag(Long tagId, TagResource tagResource) throws TagNotFoundException {
    	
    	Tag tag = getTag(tagId);
    	
    	BeanUtils.copyProperties(tagResource, tag);
    	
    	tag = tagRepository.save(tag);
    	
    	return tag;
    }
    
    public Tag createTag(TagResource tagResource) {
    	
    	Tag tag = new Tag(tagResource);
    	
    	tag = tagRepository.save(tag);
    	
    	return tag;
    }
    
    public void deleteTag(Long tagId) {
    	
    	tagRepository.delete(tagId);
    }
    
    @Transactional(readOnly = true)    
    public Page<Tag> searchTags(List<TagSearchResource> searchResources, Pageable pageable) {   	
    	
    	Page<Tag> page = tagRepository.findAll(buildSearchSpecifications(searchResources), pageable);
    	
    	return page;
    }

	@Override
	public Specification<Tag> processSearchResource(TagSearchResource searchResource) {

		if (!searchResource.getNameLike().isEmpty()) {
			
			return Tag.nameLike(searchResource.getNameLike());
			
		} else if (!searchResource.getDescriptionLike().isEmpty()) {
			
			return Tag.descriptionLike(searchResource.getDescriptionLike());
		}
				
		return null;
	}
    
}
