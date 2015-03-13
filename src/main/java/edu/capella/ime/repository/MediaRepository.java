package edu.capella.ime.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.capella.ime.domain.Media;

public interface MediaRepository extends JpaRepository<Media, Long>, JpaSpecificationExecutor<Media> {
	
	@Query("SELECT m FROM Media m INNER JOIN m.applications a WHERE a.id IN (:applicationIds)")
	Page<Media> findByApplicationIds(@Param("applicationIds") Collection<Long> applicationIds, Pageable pageable);
	
	@Query("SELECT m FROM Media m INNER JOIN m.applications a WHERE a.id IN (:applicationIds)")
	List<Media> findByApplicationIds(@Param("applicationIds") Collection<Long> applicationIds);	
}
