package edu.capella.ime.service;

import static org.springframework.data.jpa.domain.Specifications.not;
import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.capella.ime.domain.Media;
import edu.capella.ime.domain.Tag;
import edu.capella.ime.repository.TagRepository;
import edu.capella.ime.util.BooleanOperation;
import edu.capella.ime.web.rest.resource.TagResource;
import edu.capella.ime.web.rest.resource.TagSearchResource;

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
    
    public Page<Tag> searchTags(List<TagSearchResource> searchResources, Pageable pageable) {
    	
    	List<Specification<Tag>> specs = new ArrayList<>();
    	LinkedList<BooleanOperation> joiningOperations = new LinkedList<>();
    	
    	for (TagSearchResource searchResource : searchResources) {
    		
    		if (!searchResource.getNameLike().isEmpty()) {
    			
    			specs.add(Tag.nameLike(searchResource.getNameLike()));
    			
    		} else if (!searchResource.getDescriptionLike().isEmpty()) {
    			
    			specs.add(Tag.descriptionLike(searchResource.getDescriptionLike()));
    		}
    		
    		if (searchResource.getBooleanOperation() != BooleanOperation.NONE) {
    			joiningOperations.add(searchResource.getBooleanOperation());
    		}
    	}
    	
    	if (specs.isEmpty()) {
    		// TODO throw exception
    		log.warn("searchTags: no valid specifications supplied");    		
    	}
    	
    	int specIndex = 0;
    	Specifications<Tag> searchSpecs = where(specs.get(specIndex));
    	loop: for (BooleanOperation joinOperation : joiningOperations) {
    		
    		if (++specIndex > specs.size() - 1) {
    			break;
    		}
    		
    		switch (joinOperation) {
			case AND:
				searchSpecs = searchSpecs.and(specs.get(specIndex));
				break;
				
			case AND_NOT:
				searchSpecs = searchSpecs.and(not(specs.get(specIndex)));
				break;
				
			case OR:
				searchSpecs = searchSpecs.or(specs.get(specIndex));
				break;
				
			case OR_NOT:
				searchSpecs = searchSpecs.or(not(specs.get(specIndex)));
				break;
				
			case NONE:
				break loop;
				
			default:
				log.warn("searchTags: unkown boolean operation encountered");
				break loop;
    		}
    	}    	
    	
    	Page<Tag> page = tagRepository.findAll(searchSpecs, pageable);
    	
    	return page;
    }
    
}
