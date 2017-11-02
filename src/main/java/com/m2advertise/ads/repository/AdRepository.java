package com.m2advertise.ads.repository;

import com.m2advertise.ads.domain.Ad;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Ad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
	
	Page<Ad> findAllByAdvertiser(Pageable pageable, String advertiser);
}
