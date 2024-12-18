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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a piece of threat intelligence data in the IntelliSIEM system.
 *
 * <p>This entity is mapped to the 'threat_intelligence' table in the database.</p>
 */
@Entity
@Table(name = "threat_intelligence")
public class ThreatIntelligence {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreatIntelligence.class);

    /**
     * The unique identifier for the threat intelligence record.
     * This is the primary key and is auto-incremented.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The type of threat, such as "Malware", "CVE", "IP", or "Domain".
     * Cannot be blank.
     */
    @Column(nullable = false, name = "threat_type", length = 50)
    @NotBlank(message = "Threat type cannot be blank.")
    private String threatType;

    /**
     * The specific value of the threat (e.g., a CVE ID, IP address, or domain name).
     * Cannot be blank.
     */
    @Column(nullable = false, name = "value", columnDefinition = "TEXT")
    @NotBlank(message = "Threat value cannot be blank.")
    private String value;

    /**
     * A description of the threat. Optional.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * The severity of the threat. Must be one of 'critical', 'high', 'medium', or 'low'.
     * Cannot be null.
     */
    @Column(nullable = false, length = 10)
    @NotNull(message = "Severity must be specified.")
    private String severity;

    /**
     * The timestamp when the threat was first seen. Optional.
     */
    @Column(name = "first_seen")
    private LocalDateTime firstSeen;

    /**
     * The timestamp when the threat was last seen. Optional.
     */
    @Column(name = "last_seen")
    private LocalDateTime lastSeen;

    /**
     * The timestamp when the record was created.
     * This value is set automatically on creation.
     */
    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor.
     */
    public ThreatIntelligence() {
        LOGGER.debug("ThreatIntelligence entity initialized with default constructor.");
    }

    /**
     * Constructor with parameters.
     *
     * @param threatType the type of threat (must not be blank).
     * @param value the value of the threat (must not be blank).
     * @param description a description of the threat.
     * @param severity the severity of the threat (must not be null).
     * @param firstSeen the first seen timestamp of the threat.
     * @param lastSeen the last seen timestamp of the threat.
     */
    public ThreatIntelligence(String threatType, String value, String description,
                              String severity, LocalDateTime firstSeen, LocalDateTime lastSeen) {
        if (threatType == null || threatType.trim().isEmpty()) {
            LOGGER.error("Threat type cannot be null or blank when creating ThreatIntelligence.");
            throw new IllegalArgumentException("Threat type cannot be null or blank when creating ThreatIntelligence.");
        }
        if (value == null || value.trim().isEmpty()) {
            LOGGER.error("Threat value cannot be null or blank when creating ThreatIntelligence.");
            throw new IllegalArgumentException("Threat value cannot be null or blank when creating ThreatIntelligence.");
        }
        if (severity == null || severity.trim().isEmpty()) {
            LOGGER.error("Threat severity cannot be null or blank when creating ThreatIntelligence.");
            throw new IllegalArgumentException("Threat severity cannot be null or blank when creating ThreatIntelligence.");
        }

        this.threatType = threatType;
        this.value = value;
        this.description = description;
        this.severity = severity;
        this.firstSeen = firstSeen;
        this.lastSeen = lastSeen;

        LOGGER.debug("ThreatIntelligence created with type '{}' and value '{}'.", threatType, value);
        if (firstSeen != null) {
            LOGGER.debug("ThreatIntelligence created with first seen timestamp '{}'.", firstSeen);
        }
        if (lastSeen != null) {
            LOGGER.debug("ThreatIntelligence created with last seen timestamp '{}'.", lastSeen);
        }
        if (description != null) {
            LOGGER.debug("ThreatIntelligence created with description '{}'.", description);
        }
    }

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getThreatType() {
        return threatType;
    }

    public void setThreatType(String threatType) {
        if (threatType == null || threatType.trim().isEmpty()) {
            LOGGER.error("Attempted to set a null or blank threat type on ThreatIntelligence.");
            throw new IllegalArgumentException("Attempted to set a null or blank threat type on ThreatIntelligence.");
        }
        this.threatType = threatType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            LOGGER.error("Attempted to set a null or blank threat value on ThreatIntelligence.");
            throw new IllegalArgumentException("Attempted to set a null or blank threat value on ThreatIntelligence.");
        }
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        if (severity == null || severity.trim().isEmpty()) {
            LOGGER.error("Attempted to set a null or blank threat severity on ThreatIntelligence.");
            throw new IllegalArgumentException("Attempted to set a null or blank threat severity on ThreatIntelligence.");
        }
        this.severity = severity;
    }

    public LocalDateTime getFirstSeen() {
        return firstSeen;
    }

    public void setFirstSeen(LocalDateTime firstSeen) {
        this.firstSeen = firstSeen;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
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
        LOGGER.debug("ThreatIntelligence created with timestamp '{}'.", createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ThreatIntelligence that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ThreatIntelligence{" +
                "id=" + id +
                ", threatType='" + threatType + '\'' +
                ", value='" + value + '\'' +
                ", severity='" + severity + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
