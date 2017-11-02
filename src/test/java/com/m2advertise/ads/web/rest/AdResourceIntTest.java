package com.m2advertise.ads.web.rest;

import com.m2advertise.ads.M2AdvertiseAdsApp;

import com.m2advertise.ads.config.SecurityBeanOverrideConfiguration;

import com.m2advertise.ads.domain.Ad;
import com.m2advertise.ads.repository.AdRepository;
import com.m2advertise.ads.service.AdService;
import com.m2advertise.ads.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AdResource REST controller.
 *
 * @see AdResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {M2AdvertiseAdsApp.class, SecurityBeanOverrideConfiguration.class})
public class AdResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COVER = "AAAAAAAAAA";
    private static final String UPDATED_COVER = "BBBBBBBBBB";

    private static final String DEFAULT_ADVERTISER = "AAAAAAAAAA";
    private static final String UPDATED_ADVERTISER = "BBBBBBBBBB";

    private static final String DEFAULT_GAME_CONFIGURATION_ID = "AAAAAAAAAA";
    private static final String UPDATED_GAME_CONFIGURATION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_GAME_ID = "AAAAAAAAAA";
    private static final String UPDATED_GAME_ID = "BBBBBBBBBB";

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private AdService adService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAdMockMvc;

    private Ad ad;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AdResource adResource = new AdResource(adService);
        this.restAdMockMvc = MockMvcBuilders.standaloneSetup(adResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ad createEntity(EntityManager em) {
        Ad ad = new Ad()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .cover(DEFAULT_COVER)
            .advertiser(DEFAULT_ADVERTISER)
            .gameConfigurationId(DEFAULT_GAME_CONFIGURATION_ID)
            .gameId(DEFAULT_GAME_ID);
        return ad;
    }

    @Before
    public void initTest() {
        ad = createEntity(em);
    }

    @Test
    @Transactional
    public void createAd() throws Exception {
        int databaseSizeBeforeCreate = adRepository.findAll().size();

        // Create the Ad
        restAdMockMvc.perform(post("/api/ads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ad)))
            .andExpect(status().isCreated());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeCreate + 1);
        Ad testAd = adList.get(adList.size() - 1);
        assertThat(testAd.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAd.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAd.getCover()).isEqualTo(DEFAULT_COVER);
        assertThat(testAd.getAdvertiser()).isEqualTo(DEFAULT_ADVERTISER);
        assertThat(testAd.getGameConfigurationId()).isEqualTo(DEFAULT_GAME_CONFIGURATION_ID);
        assertThat(testAd.getGameId()).isEqualTo(DEFAULT_GAME_ID);
    }

    @Test
    @Transactional
    public void createAdWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adRepository.findAll().size();

        // Create the Ad with an existing ID
        ad.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdMockMvc.perform(post("/api/ads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ad)))
            .andExpect(status().isBadRequest());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = adRepository.findAll().size();
        // set the field null
        ad.setTitle(null);

        // Create the Ad, which fails.

        restAdMockMvc.perform(post("/api/ads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ad)))
            .andExpect(status().isBadRequest());

        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdvertiserIsRequired() throws Exception {
        int databaseSizeBeforeTest = adRepository.findAll().size();
        // set the field null
        ad.setAdvertiser(null);

        // Create the Ad, which fails.

        restAdMockMvc.perform(post("/api/ads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ad)))
            .andExpect(status().isBadRequest());

        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAds() throws Exception {
        // Initialize the database
        adRepository.saveAndFlush(ad);

        // Get all the adList
        restAdMockMvc.perform(get("/api/ads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ad.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].cover").value(hasItem(DEFAULT_COVER.toString())))
            .andExpect(jsonPath("$.[*].advertiser").value(hasItem(DEFAULT_ADVERTISER.toString())))
            .andExpect(jsonPath("$.[*].gameConfigurationId").value(hasItem(DEFAULT_GAME_CONFIGURATION_ID.toString())))
            .andExpect(jsonPath("$.[*].gameId").value(hasItem(DEFAULT_GAME_ID.toString())));
    }

    @Test
    @Transactional
    public void getAd() throws Exception {
        // Initialize the database
        adRepository.saveAndFlush(ad);

        // Get the ad
        restAdMockMvc.perform(get("/api/ads/{id}", ad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ad.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.cover").value(DEFAULT_COVER.toString()))
            .andExpect(jsonPath("$.advertiser").value(DEFAULT_ADVERTISER.toString()))
            .andExpect(jsonPath("$.gameConfigurationId").value(DEFAULT_GAME_CONFIGURATION_ID.toString()))
            .andExpect(jsonPath("$.gameId").value(DEFAULT_GAME_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAd() throws Exception {
        // Get the ad
        restAdMockMvc.perform(get("/api/ads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAd() throws Exception {
        // Initialize the database
        adService.save(ad);

        int databaseSizeBeforeUpdate = adRepository.findAll().size();

        // Update the ad
        Ad updatedAd = adRepository.findOne(ad.getId());
        updatedAd
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .cover(UPDATED_COVER)
            .advertiser(UPDATED_ADVERTISER)
            .gameConfigurationId(UPDATED_GAME_CONFIGURATION_ID)
            .gameId(UPDATED_GAME_ID);

        restAdMockMvc.perform(put("/api/ads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAd)))
            .andExpect(status().isOk());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeUpdate);
        Ad testAd = adList.get(adList.size() - 1);
        assertThat(testAd.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAd.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAd.getCover()).isEqualTo(UPDATED_COVER);
        assertThat(testAd.getAdvertiser()).isEqualTo(UPDATED_ADVERTISER);
        assertThat(testAd.getGameConfigurationId()).isEqualTo(UPDATED_GAME_CONFIGURATION_ID);
        assertThat(testAd.getGameId()).isEqualTo(UPDATED_GAME_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingAd() throws Exception {
        int databaseSizeBeforeUpdate = adRepository.findAll().size();

        // Create the Ad

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAdMockMvc.perform(put("/api/ads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ad)))
            .andExpect(status().isCreated());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAd() throws Exception {
        // Initialize the database
        adService.save(ad);

        int databaseSizeBeforeDelete = adRepository.findAll().size();

        // Get the ad
        restAdMockMvc.perform(delete("/api/ads/{id}", ad.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ad.class);
        Ad ad1 = new Ad();
        ad1.setId(1L);
        Ad ad2 = new Ad();
        ad2.setId(ad1.getId());
        assertThat(ad1).isEqualTo(ad2);
        ad2.setId(2L);
        assertThat(ad1).isNotEqualTo(ad2);
        ad1.setId(null);
        assertThat(ad1).isNotEqualTo(ad2);
    }
}
