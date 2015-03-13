package edu.capella.ime.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.capella.ime.domain.Tag;
import edu.capella.ime.repository.TagRepository;
import edu.capella.ime.web.rest.resource.TagResource;

@Service
@Transactional
public class TagService {
	
    private final Logger log = LoggerFactory.getLogger(TagService.class);
    
    private TagRepository tagRepository;
    
    @Autowired
    public TagService(TagRepository tagRepository) {
    	
    	this.tagRepository = tagRepository;
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
    public Tag getTag(Long tagId) {
    	
    	Tag tag = tagRepository.findOne(tagId);
    	
    	return tag;
    }
    
    @Transactional(readOnly = true)
    public List<Tag> getTagsForMedia(Long mediaId) {
    	
    	List<Tag> tags = tagRepository.findByMediaIds(Arrays.asList(mediaId));
    	
    	return tags;
    }
    
    @Transactional(readOnly = true)
    public Page<Tag> getTagsForMedia(Long mediaId, Pageable pageable) {
    	
    	Page<Tag> page = tagRepository.findByMediaIds(Arrays.asList(mediaId), pageable);
    	
    	return page;
    }
    
    public Tag updateTag(Long tagId, TagResource tagResource) {
    	
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
    
}
