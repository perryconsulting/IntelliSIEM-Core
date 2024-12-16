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
 * Represents an IP address associated with an asset in the IntelliSIEM system.
 *
 * <p>This entity is mapped to the 'ip_address' table in the database.</p>
 */
@Entity
@Table(schema = "intellisiem", name = "ip_address")
public class IPAddress {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPAddress.class);

    /**
     * The unique identifier for the IP address record.
     * This is the primary key and is auto-incremented.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The associated asset to which this IP address belongs.
     * This field establishes a many-to-one relationship with the {@link Asset} entity.
     * Cannot be null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "Asset cannot be null.")
    private Asset asset;

    /**
     * The IP address string, which can store both IPv4 and IPv6 addresses.
     * Cannot be blank.
     */
    @Column(nullable = false, name = "ip_address", length = 39)
    @NotBlank(message = "IP cannot be blank.")
    private String ip;

    /**
     * The timestamp when the IP address record was created.
     * This value is set automatically on creation.
     */
    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor.
     */
    public IPAddress() {
        LOGGER.debug("IPAddress entity initialized with default constructor.");
    }

    /**
     * Constructor with parameters.
     *
     * @param asset the associated asset (must not be null).
     * @param ip the IP address string (must not be blank).
     */
    public IPAddress(Asset asset, String ip) {
        if (asset == null) {
            LOGGER.error("Asset cannot be null when creating IPAddress.");
            throw new IllegalArgumentException("Asset cannot be null when creating IPAddress.");
        }
        if (ip == null || ip.trim().isEmpty()) {
            LOGGER.error("IP cannot be null or blank when creating IPAddress.");
            throw new IllegalArgumentException("IP cannot be null or blank when creating IPAddress.");
        }
        this.asset = asset;
        this.ip = ip;

        LOGGER.debug("IPAddress created with asset ID {} and IP address '{}'.", asset.getId(), ip);
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
            LOGGER.error("Attempted to set a null asset on IPAddress.");
            throw new IllegalArgumentException("Attempted to set a null asset on IPAddress.");
        }
        this.asset = asset;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            LOGGER.error("Attempted to set a null or blank IP address on IPAddress.");
            throw new IllegalArgumentException("Attempted to set a null or blank IP address on IPAddress.");
        }
        this.ip = ip;
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
        LOGGER.debug("IPAddress persisted with creation timestamp {}.", createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IPAddress that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "IPAddress{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
