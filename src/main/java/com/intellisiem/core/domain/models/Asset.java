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
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "intellisiem", name = "asset")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Primary key using UUID for uniqueness

    @Column(nullable = false, unique = true)
    private String hostname; // Asset hostname (unique within the system)

    @Column(nullable = true)
    private String fqdn; // Fully Qualified Domain Name (optional)

    @Column(nullable = true, name = "mac_address")
    private String macAddress; // MAC address of the asset

    @Column(nullable = false, name = "asset_type")
    private String assetType; // Type of the asset (e.g., Server, Workstation)

    @Column(nullable = true, name = "os_name")
    private String osName; // Operating system name (optional)

    @Column(nullable = true, name = "os_version")
    private String osVersion; // Operating system version (optional)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "criticality")
    private Criticality criticality; // Criticality level (e.g., HIGH, MEDIUM, LOW)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", referencedColumnName = "id")
    private AssetSource source; // Reference to the source of the asset (e.g., Nmap, Nessus)

    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt; // Timestamp when the asset was created

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt; // Timestamp when the asset was last updated

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
