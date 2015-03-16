package edu.capella.ime.service.search;

import static org.springframework.data.jpa.domain.Specifications.not;
import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import edu.capella.ime.util.BooleanOperation;
import edu.capella.ime.web.rest.resource.search.SearchResource;

public abstract class SearchEnabledService<E, R extends SearchResource> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public abstract Specification<E> processSearchResource(R searchResource);
	
	protected Specifications<E> buildSearchSpecifications(List<R> searchResources) {
		
    	List<Specification<E>> specs = new ArrayList<>();
    	List<BooleanOperation> joiningOperations = new ArrayList<>();
		
    	for (R searchResource : searchResources) {
    		
    		Specification<E> spec = processSearchResource(searchResource);
    		
    		if (searchResource.getBooleanOperation() != BooleanOperation.NONE) {
    			joiningOperations.add(searchResource.getBooleanOperation());
    		}
    		
    		if (spec != null) {
        		specs.add(spec);    		    			
    		}    		
    	}
    	
    	if (specs.isEmpty()) {
    		// TODO throw exception
    		log.warn("no valid specifications supplied");    		
    	}
    	
    	int specIndex = 0;
    	Specifications<E> searchSpecs = where(specs.get(specIndex));
    	
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
				log.warn("unkown boolean operation encountered");
				break loop;
    		}
    	}    	
		
    	return searchSpecs;
	}
	
}
