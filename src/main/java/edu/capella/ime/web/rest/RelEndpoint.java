package edu.capella.ime.web.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rels")
public class RelEndpoint {

	@SuppressWarnings("serial")
	private static final Map<String, String> RELS = new HashMap<String, String>() {
		{
			put("all", "request the entities as a complete list rather than paginating the results");
			put("media", "get the media associated with this entity");
			put("search", "search across all entities which are returned by the parent endpoint");
			put("tags", "get the tags associated with the entity");			
		}
	};
	
    @RequestMapping(method = RequestMethod.GET, value = "/{rel}", produces = MediaType.TEXT_HTML_VALUE)	
	public ResponseEntity<String> getRel(@PathVariable String rel) {		
		
    	String relDescription = RELS.get(rel);
    	
    	if (relDescription == null || relDescription.isEmpty()) {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
    	
		return new ResponseEntity<>(relDescription, HttpStatus.OK);
	}	
	
}
