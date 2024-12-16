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
 * Represents the source of an asset in the IntelliSIEM system.
 * Sources may include scanning tools like Nmap or Nessus.
 *
 * <p>This class is mapped to the 'asset_source' table in the database, and its fields
 * represent columns in the table.</p>
 */

@Entity
@Table(schema = "intellisiem", name = "asset_source")
public class AssetSource {

    /**
     * The unique identifier for the asset source.
     * This is the primary key and is auto-incremented.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The name of the source (e.g., "Nmap"). Cannot be blank and must be unique.
     */
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Source name cannot be blank.")
    private String name;

    /**
     * A description of the source. Optional.
     */
    @Column
    private String description;

    /**
     * The timestamp when the source was created.
     * This value is set automatically on creation.
     */
    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor
     */
    public AssetSource() {
    }

    /**
     * Constructor with parameters
     *
     * @param name the name of the asset source.
     * @param description the description of the asset source
     */
    public AssetSource(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof AssetSource that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AssetSource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
