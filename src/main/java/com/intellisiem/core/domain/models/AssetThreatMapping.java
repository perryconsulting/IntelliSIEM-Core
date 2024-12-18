/*
 * Copyright (c) 2024 Rob Perry
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellisiem.core.domain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a mapping between an asset and a threat in the IntelliSIEM system.
 *
 * <p>This class is mapped to the 'asset_threat_mapping' table in the database and
 * includes a calculated relevance score for prioritization.</p>
 */
@Entity
@Table(name = "asset_threat_mapping")
public class AssetThreatMapping {

    public static final Logger LOGGER = LoggerFactory.getLogger(AssetThreatMapping.class);

    /**
     * The unique identifier for the asset-threat mapping record.
     * This is the primary key and is auto-incremented.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The associated asset in the mapping.
     * This field establishes a many-to-one relationship with the {@link Asset} entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "Asset cannot be null.")
    private Asset asset;

    /**
     * The associated threat in the mapping.
     * This field establishes a many-to-one relationship with the {@link ThreatIntelligence} entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "threat_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "Threat cannot be null.")
    private ThreatIntelligence threat;

    /**
     * The relevance score of the threat to the asset.
     * This is a calculated score for prioritization.
     */
    @Column(nullable = false, name = "relevance_score", precision = 5, scale = 2)
    @NotNull(message = "Relevance score cannot be null.")
    private Double relevanceScore;

    /**
     * The timestamp when the record was created.
     * This value is set automatically on creation.
     */
    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor.
     */
    public AssetThreatMapping() {
        LOGGER.debug("AssetThreatMapping entity initialized with default constructor.");
    }

    /**
     * Constructor with parameters.
     *
     * @param asset the associated asset (must not be null).
     * @param threat the associated threat (must not be null).
     * @param relevanceScore the relevance score of the mapping (must not be null).
     */
    public AssetThreatMapping(Asset asset, ThreatIntelligence threat, Double relevanceScore) {
        if (asset == null) {
            LOGGER.error("Asset cannot be null when creating AssetThreatMapping.");
            throw new IllegalArgumentException("Asset cannot be null.");
        }
        if (threat == null) {
            LOGGER.error("Threat cannot be null when creating AssetThreatMapping.");
            throw new IllegalArgumentException("Threat cannot be null.");
        }
        if (relevanceScore == null) {
            LOGGER.error("Relevance score cannot be null when creating AssetThreatMapping.");
            throw new IllegalArgumentException("Relevance score cannot be null.");
        }

        this.asset = asset;
        this.threat = threat;
        this.relevanceScore = relevanceScore;

        LOGGER.debug("AssetThreatMapping created with asset ID {}, threat ID {}, and relevance score {}.",
                asset.getId(), threat.getId(), relevanceScore);
    }

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        if (asset == null) {
            LOGGER.error("Attempted to set a null asset on AssetThreatMapping.");
            throw new IllegalArgumentException("Attempted to set a null asset on AssetThreatMapping.");
        }
        this.asset = asset;
    }

    public ThreatIntelligence getThreat() {
        return threat;
    }

    public void setThreat(ThreatIntelligence threat) {
        if (threat == null) {
            LOGGER.error("Attempted to set a null threat on AssetThreatMapping.");
            throw new IllegalArgumentException("Attempted to set a null threat on AssetThreatMapping.");
        }
        this.threat = threat;
    }

    public Double getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(Double relevanceScore) {
        if (relevanceScore == null) {
            LOGGER.error("Attempted to set a null relevance score on AssetThreatMapping.");
            throw new IllegalArgumentException("Attempted to set a null relevance score on AssetThreatMapping.");
        }
        this.relevanceScore = relevanceScore;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Lifecycle hook to set the creation timestamp before the entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        LOGGER.debug("AssetThreatMapping created with ID {} at {}.", this.id, this.createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetThreatMapping that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AssetThreatMapping{" +
                "id=" + id +
                ", relevanceScore=" + relevanceScore +
                ", createdAt=" + createdAt +
                '}';
    }
}
