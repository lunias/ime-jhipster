package edu.capella.ime.web.rest.resource.search;

import org.springframework.hateoas.ResourceSupport;

import edu.capella.ime.util.BooleanOperation;

public class TagSearchResource extends ResourceSupport implements SearchResource {

	private String nameLike = "";
	
	private String descriptionLike = "";
	
	private BooleanOperation booleanOperation = BooleanOperation.NONE;

	public String getNameLike() {
		return nameLike;
	}

	public void setNameLike(String nameLike) {
		this.nameLike = nameLike;
	}

	public String getDescriptionLike() {
		return descriptionLike;
	}

	public void setDescriptionLike(String descriptionLike) {
		this.descriptionLike = descriptionLike;
	}

	@Override
	public BooleanOperation getBooleanOperation() {
		return booleanOperation;
	}

	public void setBooleanOperation(BooleanOperation booleanOperation) {
		this.booleanOperation = booleanOperation;
	}			
}
