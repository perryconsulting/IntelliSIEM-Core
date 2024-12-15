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
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Represents a piece of threat intelligence data in the IntelliSIEM system.
 *
 * <p>This class is mapped to the 'threat_intelligence' table in the database.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "intellisiem", name = "threat_intelligence")
@ToString(onlyExplicitlyIncluded = true)
public class ThreatIntelligence {

    /**
     * The unique identifier for the threat intelligence record.
     * This is the primary key and is auto-incremented.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Integer id;

    /**
     * The type of threat, such as "Malware", "CVE", "IP", or "Domain".
     * Cannot be blank.
     */
    @Column(nullable = false, name = "threat_type", length = 50)
    @NotBlank(message = "Threat type cannot be blank.")
    @ToString.Include
    private String threatType;

    /**
     * The specific value of the threat (e.g., a CVE ID, IP address, or domain name).
     * Cannot be blank.
     */
    @Column(nullable = false, name = "value", columnDefinition = "TEXT")
    @NotBlank(message = "Threat value cannot be blank.")
    @ToString.Include
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
    @ToString.Include
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
     * Lifecycle hook to set the creation timestamp before the entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
