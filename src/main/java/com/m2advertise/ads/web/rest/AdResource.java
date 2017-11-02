package com.m2advertise.ads.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.m2advertise.ads.domain.Ad;
import com.m2advertise.ads.service.AdService;
import com.m2advertise.ads.web.rest.util.HeaderUtil;
import com.m2advertise.ads.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Ad.
 */
@RestController
@RequestMapping("/api")
public class AdResource {

    private final Logger log = LoggerFactory.getLogger(AdResource.class);

    private static final String ENTITY_NAME = "ad";

    private final AdService adService;

    public AdResource(AdService adService) {
        this.adService = adService;
    }

    /**
     * POST  /ads : Create a new ad.
     *
     * @param ad the ad to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ad, or with status 400 (Bad Request) if the ad has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ads")
    @Timed
    public ResponseEntity<Ad> createAd(@Valid @RequestBody Ad ad) throws URISyntaxException {
        log.debug("REST request to save Ad : {}", ad);
        if (ad.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ad cannot already have an ID")).body(null);
        }
        Ad result = adService.save(ad);
        return ResponseEntity.created(new URI("/api/ads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ads : Updates an existing ad.
     *
     * @param ad the ad to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ad,
     * or with status 400 (Bad Request) if the ad is not valid,
     * or with status 500 (Internal Server Error) if the ad couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ads")
    @Timed
    public ResponseEntity<Ad> updateAd(@Valid @RequestBody Ad ad) throws URISyntaxException {
        log.debug("REST request to update Ad : {}", ad);
        if (ad.getId() == null) {
            return createAd(ad);
        }
        Ad result = adService.save(ad);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ad.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ads : get all the ads.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ads in body
     */
    @GetMapping("/ads")
    @Timed
    public ResponseEntity<List<Ad>> getAllAds(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Ads");
        Page<Ad> page = adService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /ads/ : get all the ads by Advertiser.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ads in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/ads/advertiser/{advertiserId}")
    @Timed
    public ResponseEntity<List<Ad>> getAllAdsByAdvertiser(@ApiParam Pageable pageable, @PathVariable String advertiserId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Ads for the advertiser "+advertiserId);
        Page<Ad> page = adService.findAllByAdvertiser(pageable,advertiserId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ads/advertiser"+"/"+advertiserId);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /ads/:id : get the "id" ad.
     *
     * @param id the id of the ad to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ad, or with status 404 (Not Found)
     */
    @GetMapping("/ads/{id}")
    @Timed
    public ResponseEntity<Ad> getAd(@PathVariable Long id) {
        log.debug("REST request to get Ad : {}", id);
        Ad ad = adService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ad));
    }

    /**
     * DELETE  /ads/:id : delete the "id" ad.
     *
     * @param id the id of the ad to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ads/{id}")
    @Timed
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        log.debug("REST request to delete Ad : {}", id);
        adService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
}
