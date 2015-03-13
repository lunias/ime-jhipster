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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.EntityType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;

import edu.capella.ime.web.rest.resource.MediaResource;

@Entity
@Table(name = "T_MEDIA")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Media extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Lob
	@Column(name = "media_value")
	private String mediaValue;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy="mediaItems")
	private Set<Application> applications = new HashSet<>();
	
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "MEDIA_TAG",
            joinColumns = {@JoinColumn(name = "media_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})	
	Set<Tag> tags = new HashSet<>();	
    
	public Media(String mediaValue) {
		
		this.mediaValue = mediaValue;
	}    
    
	public Media(MediaResource mediaResource) {
		
		BeanUtils.copyProperties(mediaResource, this);
	}    
    
	public Media() {

	}		

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMediaValue() {
		return mediaValue;
	}

	public void setMediaValue(String mediaValue) {
		this.mediaValue = mediaValue;
	}
	
	public Set<Tag> getTags() {
		return tags;
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
		Media other = (Media) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Media [id=" + id + ", mediaValue=" + mediaValue + "]";
	}
	
	public static Specification<Media> hasAnyOfTags(final String... tagNames) {
		
		return new Specification<Media>() {

			@Override
			public Predicate toPredicate(Root<Media> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				EntityType<Media> media_ = root.getModel();
				
				SetJoin<Media, Tag> mediaTagNames = root.join(media_.getSet("tags", Tag.class));				
				
				query.distinct(true);
				
				return mediaTagNames.get("name").in((Object[]) tagNames);									
			}
		};
	}
	
	public static Specification<Media> hasNoneOfTags(final String... tagNames) {
		
		return new Specification<Media>() {

			@Override
			public Predicate toPredicate(Root<Media> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				EntityType<Media> media_ = root.getModel();				
				
				Subquery<Long> mediaWithTagsQ = query.subquery(Long.class);
				Root<Media> mediaWithTagsRoot = mediaWithTagsQ.from(Media.class);
				SetJoin<Media, Tag> mediaTagNames = mediaWithTagsRoot.join(media_.getSet("tags", Tag.class));				
				
				mediaWithTagsQ.select(mediaWithTagsRoot.get("id"))
					.where(mediaTagNames.get("name").in((Object[]) tagNames))
					.distinct(true);
				
				return root.get("id").in(mediaWithTagsQ).not();
			}
			
		};
	}
	
	public static Specification<Media> hasAllOfTags(final String... tagNames) {
		
		return new Specification<Media>() {

			@Override
			public Predicate toPredicate(Root<Media> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				EntityType<Media> media_ = root.getModel();
				
				SetJoin<Media, Tag> mediaTagNames = root.join(media_.getSet("tags", Tag.class));
				
				query.groupBy(root.get("id"))
					.having(cb.equal(cb.count(root), tagNames.length));
				
				return mediaTagNames.get("name").in((Object[]) tagNames);
			}
		};
	}
}
