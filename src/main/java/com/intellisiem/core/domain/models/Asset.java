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
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;
import java.time.LocalDateTime;

/**
 * Represents an asset in the IntelliSIEM system.
 * Each asset corresponds to a physical or virtual device within the monitored environment.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "intellisiem", name = "asset")
@ToString(onlyExplicitlyIncluded = true) // Avoid the inclusion of lazy-loaded relationships
public class Asset {

    /**
     * The unique identifier for the asset.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ToString.Include
    private UUID id;

    /**
     * The hostname of the asset. Cannot be blank and must be unique.
     */
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Hostname cannot be blank.")
    @ToString.Include
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
    @ToString.Exclude // Prevents lazy loading during toString()
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
     * Lifecycle hook to set the creation and update timestamps before the entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Lifecycle hook to update the timestamp before the entity is updated.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Determines equality based on the unique identifier (ID) of the asset.
     * Transient entities (ID is null) are not considered equal to any other entity.
     *
     * @param o the object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Asset asset)) return false;
        return id != null && id.equals(asset.id);
    }

    /**
     * Generates a hash code based on the unique identifier (ID) of the asset.
     * If the ID is null (transient entity), a constant hash code is returned.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
