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

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an IP address associated with an asset in the IntelliSIEM system.
 *
 * <p>This class is mapped to the 'ip_address' table in the database.</p>
 */
@Entity
@Table(schema = "intellisiem", name = "ip_address")
public class IPAddress {

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
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", referencedColumnName = "id", nullable = false)
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
    public IPAddress() {}

    /**
     * Constructor with parameters.
     *
     * @param asset the associated asset.
     * @param ip the IP address string.
     */
    public IPAddress(Asset asset, String ip) {
        this.asset = asset;
        this.ip = ip;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
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
