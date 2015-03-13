package edu.capella.ime.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.BeanUtils;

import edu.capella.ime.web.rest.resource.TagResource;

@Entity
@Table(name = "T_TAG")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tag extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @NotNull
    @Pattern(regexp = "^[A-Z_-]*$")
    @Size(min = 1, max = 256)
    @Column(length = 256, unique = true, nullable = false)		
	private String name;
	
    @Size(min = 1, max = 256)
    @Column(length = 256)	
	private String description = "";
    
	@ManyToMany(fetch = FetchType.LAZY, mappedBy="tags")
	private Set<Media> taggedMedia = new HashSet<>();    

    public Tag(String name, String description) {
    	
    	this.name = name;
    	this.description = description;
    }    
    
    public Tag(String name) {
    	
    	this(name, "");
    }
    
    public Tag(TagResource tagResource) {
    	
    	BeanUtils.copyProperties(tagResource, this);
    }
    
    public Tag() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tag [id=" + id + ", name=" + name + ", description="
				+ description + "]";
	}        
}
