package edu.capella.ime.web.rest.resource;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MediaSearchResource {

	public enum BooleanOperation {
		AND, OR, AND_NOT, OR_NOT, NONE
	}
	
	private List<String> anyOfTags = new ArrayList<>();
	
	private List<String> noneOfTags = new ArrayList<>();	
	
	private List<String> allOfTags = new ArrayList<>();
	
	private BooleanOperation booleanOperation = BooleanOperation.NONE;

	public List<String> getAnyOfTags() {
		return anyOfTags;
	}

	@JsonIgnore
	public String[] getAnyOfTagsArray() {
		
		return anyOfTags.toArray(new String[anyOfTags.size()]);
	}	
	
	public void setAnyOfTags(List<String> anyOfTags) {
		this.anyOfTags = anyOfTags;
	}

	public List<String> getNoneOfTags() {
		return noneOfTags;
	}
	
	@JsonIgnore
	public String[] getNoneOfTagsArray() {
		
		return noneOfTags.toArray(new String[noneOfTags.size()]);
	}

	public void setNoneOfTags(List<String> noneOfTags) {
		this.noneOfTags = noneOfTags;
	}

	public List<String> getAllOfTags() {
		return allOfTags;
	}

	@JsonIgnore
	public String[] getAllOfTagsArray() {
		
		return allOfTags.toArray(new String[allOfTags.size()]);
	}

	public void setAllOfTags(List<String> allOfTags) {
		this.allOfTags = allOfTags;
	}

	public BooleanOperation getBooleanOperation() {
		return booleanOperation;
	}

	public void setBooleanOperation(BooleanOperation booleanOperation) {
		this.booleanOperation = booleanOperation;
	}		
}
