package com.m2advertise.ads.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Ad.
 */
@Entity
@Table(name = "ad")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "cover")
    private String cover;

    @NotNull
    @Column(name = "advertiser", nullable = false)
    private String advertiser;

    @Column(name = "game_configuration_id")
    private String gameConfigurationId;

    @Column(name = "game_id")
    private String gameId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Ad title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Ad description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public Ad cover(String cover) {
        this.cover = cover;
        return this;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAdvertiser() {
        return advertiser;
    }

    public Ad advertiser(String advertiser) {
        this.advertiser = advertiser;
        return this;
    }

    public void setAdvertiser(String advertiser) {
        this.advertiser = advertiser;
    }

    public String getGameConfigurationId() {
        return gameConfigurationId;
    }

    public Ad gameConfigurationId(String gameConfigurationId) {
        this.gameConfigurationId = gameConfigurationId;
        return this;
    }

    public void setGameConfigurationId(String gameConfigurationId) {
        this.gameConfigurationId = gameConfigurationId;
    }

    public String getGameId() {
        return gameId;
    }

    public Ad gameId(String gameId) {
        this.gameId = gameId;
        return this;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ad ad = (Ad) o;
        if (ad.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ad.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ad{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", cover='" + getCover() + "'" +
            ", advertiser='" + getAdvertiser() + "'" +
            ", gameConfigurationId='" + getGameConfigurationId() + "'" +
            ", gameId='" + getGameId() + "'" +
            "}";
    }
}
