package edu.capella.ime.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;

import edu.capella.ime.web.rest.resource.ApplicationResource;

@Entity
@Table(name = "T_APPLICATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Application extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9 ]*$")
    @Size(min = 1, max = 256)
    @Column(length = 256, unique = true, nullable = false)	
	private String name = "";
	
    @Size(min = 1, max = 256)
    @Column(length = 256)	    
	private String description = "";
	
    @NotNull    
    @Enumerated(EnumType.STRING)
	private ApplicationStatus status = ApplicationStatus.UP;
    
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "APPLICATION_MEDIA",
            joinColumns = {@JoinColumn(name = "application_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "media_id", referencedColumnName = "id")})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Media> mediaItems = new HashSet<>();

    public Application(String name, String description) {
    	
    	this.name = name;
    	this.description = description;
    }
    
    public Application(ApplicationResource applicationResource) {
    	
    	BeanUtils.copyProperties(applicationResource, this);
    }
    
    public Application() {

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

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
	
	public Set<Media> getMedia() {
		return mediaItems;
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
		Application other = (Application) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Application [id=" + id + ", name=" + name + ", description="
				+ description + ", status=" + status + "]";
	}
	
	public static Specification<Application> nameLike(final String applicationName) {
		
		return new Specification<Application>() {

			@Override
			public Predicate toPredicate(Root<Application> root, CriteriaQuery<?> query, CriteriaBuilder cb) {												
				
				return cb.like(root.<String> get("name"), '%' + applicationName + '%');
			}
		};
	}
	
	public static Specification<Application> descriptionLike(final String description) {
	
		return new Specification<Application>() {

			@Override
			public Predicate toPredicate(Root<Application> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				return cb.like(root.<String> get("description"), '%' + description + '%');
			}
		};
	}	
}
