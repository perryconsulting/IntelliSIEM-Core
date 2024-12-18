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

import com.intellisiem.core.domain.enums.Criticality;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an asset in the IntelliSIEM system.
 * Each asset corresponds to a physical or virtual device within the monitored environment.
 *
 * <p>This entity is mapped to the 'asset' table in the database.</p>
 */
@Entity
@Table(name = "asset")
public class Asset {

    private static final Logger LOGGER = LoggerFactory.getLogger(Asset.class);

    /**
     * The unique identifier for the asset.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The hostname of the asset. Cannot be blank and must be unique.
     */
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Hostname cannot be blank.")
    private String hostname;

    /**
     * The fully qualified domain name (FQDN) of the asset. Optional.
     */
    @Column
    private String fqdn;

    /**
     * The MAC address of the asset. Optional.
     */
    @Column(name = "mac_address")
    private String macAddress;

    /**
     * The type of the asset (e.g., Server, Workstation). Cannot be blank.
     */
    @Column(nullable = false, name = "asset_type")
    @NotBlank(message = "Asset type cannot be blank.")
    private String assetType;

    /**
     * The name of the operating system running on the asset. Optional.
     */
    @Column(name = "os_name")
    private String osName;

    /**
     * The version of the operating system running on the asset. Optional.
     */
    @Column(name = "os_version")
    private String osVersion;

    /**
     * The criticality level of the asset (e.g., HIGH, MEDIUM, LOW). Cannot be null.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "criticality")
    @NotNull(message = "Criticality level must be specified.")
    private Criticality criticality;

    /**
     * The source from which the asset was discovered (e.g., Nmap, Nessus). Optional.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", referencedColumnName = "id")
    private AssetSource source;

    /**
     * The timestamp when the asset was first created in the system.
     */
    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * The timestamp when the asset was last updated in the system.
     */
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public Asset() {
        LOGGER.debug("Asset entity initialized with default constructor.");
    }

    /**
     * Constructor with parameters.
     *
     * @param hostname the hostname of the asset (must not be blank).
     * @param fqdn the fully qualified domain name of the asset.
     * @param macAddress the MAC address of the asset.
     * @param assetType the type of the asset (must not be blank).
     * @param osName the operating system name.
     * @param osVersion the operating system version.
     * @param criticality the criticality of the asset (must not be null).
     * @param source the source of the asset information.
     */
    public Asset(String hostname, String fqdn, String macAddress, String assetType,
                 String osName, String osVersion, Criticality criticality, AssetSource source) {
        if (hostname == null || hostname.trim().isEmpty()) {
            LOGGER.error("Hostname cannot be blank when creating Asset.");
            throw new IllegalArgumentException("Hostname cannot be blank when creating Asset.");
        }
        if (assetType == null || assetType.trim().isEmpty()) {
            LOGGER.error("Asset type cannot be blank when creating Asset.");
            throw new IllegalArgumentException("Asset type cannot be blank when creating Asset.");
        }
        if (criticality == null) {
            LOGGER.error("Criticality must be specified when creating Asset.");
            throw new IllegalArgumentException("Criticality must not be null when creating Asset.");
        }

        this.hostname = hostname;
        this.fqdn = fqdn;
        this.macAddress = macAddress;
        this.assetType = assetType;
        this.osName = osName;
        this.osVersion = osVersion;
        this.criticality = criticality;
        this.source = source;

        LOGGER.debug("Asset created with hostname '{}' and criticality '{}'.", hostname, criticality);
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getHostname() {
        return hostname;
    }
    public void setHostname(String hostname) {
        if (hostname == null || hostname.trim().isEmpty()) {
            LOGGER.error("Attempted to set null or blank hostname to Asset entity.");
            throw new IllegalArgumentException("Attempted to set null or blank hostname to Asset entity.");
        }
        this.hostname = hostname;
    }

    public String getFqdn() {
        return fqdn;
    }

    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        if (assetType == null || assetType.trim().isEmpty()) {
            LOGGER.error("Attempted to set null or blank asset type to Asset entity.");
            throw new IllegalArgumentException("Attempted to set null or blank asset type to Asset entity.");
        }
        this.assetType = assetType;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Criticality getCriticality() {
        return criticality;
    }

    public void setCriticality(Criticality criticality) {
        if (criticality == null) {
            LOGGER.error("Attempted to set null criticality to Asset entity.");
            throw new IllegalArgumentException("Attempted to set null criticality to Asset entity.");
        }
        this.criticality = criticality;
    }

    public AssetSource getSource() {
        return source;
    }

    public void setSource(AssetSource source) {
        this.source = source;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Lifecycle hook to set the creation and update timestamps before the entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        LOGGER.debug("Asset entity initialized with hostname '{}' and criticality '{}'.", hostname, criticality);
        if (source != null) {
            LOGGER.debug("Asset entity initialized with source '{}'.", source.getName());
        } else {
            LOGGER.debug("Asset entity initialized with no source.");
        }
        if (fqdn != null && !fqdn.trim().isEmpty()) {
            LOGGER.debug("Asset entity initialized with FQDN '{}'.", fqdn);
        } else {
            LOGGER.debug("Asset entity initialized with no FQDN.");
        }
    }

    /**
     * Lifecycle hook to update the timestamp before the entity is updated.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        LOGGER.debug("Asset entity updated with hostname '{}' and criticality '{}'.", hostname, criticality);
        if (source != null) {
            LOGGER.debug("Asset entity updated with source '{}'.", source.getName());
        } else {
            LOGGER.debug("Asset entity updated with no source.");
        }
        if (fqdn != null && !fqdn.trim().isEmpty()) {
            LOGGER.debug("Asset entity updated with FQDN '{}'.", fqdn);
        } else {
            LOGGER.debug("Asset entity updated with no FQDN.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Asset asset)) return false;
        return Objects.equals(id, asset.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", hostname='" + hostname + '\'' +
                ", fqdn='" + fqdn + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", assetType='" + assetType + '\'' +
                ", osName='" + osName + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", criticality=" + criticality +
                '}';
    }
}
