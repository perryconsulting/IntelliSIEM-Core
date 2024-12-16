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

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a mapping between an asset and a threat in the IntelliSIEM system.
 *
 * <p>This class is mapped to the 'asset_threat_mapping' table in the database.</p>
 *
 * <p>The mapping includes a calculated relevance score for prioritization.</p>
 */
@Entity
@Table(schema = "intellisiem", name = "asset_threat_mapping")
public class AssetThreatMapping {

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
    private Asset asset;

    /**
     * The associated threat in the mapping.
     * This field establishes a many-to-one relationship with the {@link ThreatIntelligence} entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "threat_id", referencedColumnName = "id", nullable = false)
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
    public AssetThreatMapping() {}

    public AssetThreatMapping(Asset asset, ThreatIntelligence threat, Double relevanceScore) {
        this.asset = asset;
        this.threat = threat;
        this.relevanceScore = relevanceScore;
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
        this.asset = asset;
    }

    public ThreatIntelligence getThreat() {
        return threat;
    }

    public void setThreat(ThreatIntelligence threat) {
        this.threat = threat;
    }

    public Double getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(Double relevanceScore) {
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
