package edu.capella.ime.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.capella.ime.util.ClassScanner;

@RestController
@RequestMapping("/api")
public class RootEndpoint {	
	
    private final Logger log = LoggerFactory.getLogger(RootEndpoint.class);
	
	private static final Set<String> REQUEST_MAPPINGS;
	
	static {
		
		Set<String> requestMappings = new HashSet<>();
		
		ClassScanner requestMappingScanner = new ClassScanner().withAnnotationFilter(RequestMapping.class);
		
		for (final Class<?> clazz : requestMappingScanner.findClasses("edu.capella.ime.web.rest")) {				
			
			final RequestMapping requestMappingAnnotation = clazz.getAnnotation(RequestMapping.class);
			
			for (String value : requestMappingAnnotation.value()) {
				
				if (!value.startsWith("/api")) {
					continue;
				}
				
				requestMappings.add(value);	
			}			
		}
		
		REQUEST_MAPPINGS = Collections.unmodifiableSet(requestMappings);
	}	
	
    @RequestMapping(method = RequestMethod.GET, value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EndpointLink>> get(HttpServletRequest request) {
		
    	String requestUri;
		try {
			requestUri = new URI(request.getScheme(), 
						null, 
						request.getServerName(), 
						request.getServerPort(), 
						null, null, null)
						.toString();
			
		} catch (URISyntaxException use) {
			requestUri = "";
			log.error("Exception building request URI", use);
		}
    	
    	List<String> requestMappingList = new ArrayList<>(REQUEST_MAPPINGS);    	
    	Collections.sort(requestMappingList);
    	
    	List<EndpointLink> endpointLinks = new ArrayList<>();
    	
    	for (String rel : requestMappingList) {
    		
    		EndpointLink el = new EndpointLink();
    		el.endpoint = rel;
    		el.href = requestUri + rel;
    		
    		endpointLinks.add(el);
    	}
    	
		return new ResponseEntity<>(endpointLinks, HttpStatus.OK);
	}
    
    class EndpointLink {
    	
    	public String endpoint;
    	public String href;
    }	
}
