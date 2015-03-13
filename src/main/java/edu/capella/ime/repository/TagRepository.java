package edu.capella.ime.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.capella.ime.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

	@Query("SELECT t FROM Tag t INNER JOIN t.taggedMedia m WHERE m.id IN (:mediaIds)")
	Page<Tag> findByMediaIds(@Param("mediaIds") Collection<Long> mediaIds, Pageable pageable);
	
	@Query("SELECT t FROM Tag t INNER JOIN t.taggedMedia m WHERE m.id IN (:mediaIds)")
	List<Tag> findByMediaIds(@Param("mediaIds") Collection<Long> mediaIds);
	
}
