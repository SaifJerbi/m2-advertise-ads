package com.m2advertise.ads.service;

import com.m2advertise.ads.domain.Ad;
import com.m2advertise.ads.repository.AdRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Ad.
 */
@Service
@Transactional
public class AdService {

	private final Logger log = LoggerFactory.getLogger(AdService.class);

	private final AdRepository adRepository;

	public AdService(AdRepository adRepository) {
		this.adRepository = adRepository;
	}

	/**
	 * Save a ad.
	 *
	 * @param ad
	 *            the entity to save
	 * @return the persisted entity
	 */
	public Ad save(Ad ad) {
		log.debug("Request to save Ad : {}", ad);
		return adRepository.save(ad);
	}

	/**
	 * Get all the ads.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<Ad> findAll(Pageable pageable) {
		log.debug("Request to get all Ads");
		return adRepository.findAll(pageable);
	}

	/**
	 * Get one ad by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public Ad findOne(Long id) {
		log.debug("Request to get Ad : {}", id);
		return adRepository.findOne(id);
	}

	/**
	 * Get all the ads by current user.
	 * 
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<Ad> findAllByAdvertiser(Pageable pageable, String advertiser) {
		log.debug("Request to get all Ads by advertiser : {} " + advertiser);
		return adRepository.findAllByAdvertiser(pageable, advertiser);
	}

	/**
	 * Delete the ad by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete Ad : {}", id);
		adRepository.delete(id);
	}
}
