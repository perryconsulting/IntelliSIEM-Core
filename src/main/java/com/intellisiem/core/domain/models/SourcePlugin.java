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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a source plugin in the IntelliSIEM system.
 * Source plugins define external data sources and their configurations.
 *
 * <p>This class is mapped to the 'source_plugin' table in the database.</p>
 */
@Entity
@Table(schema = "intellisiem", name = "source_plugin")
public class SourcePlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(SourcePlugin.class);

    /**
     * The unique identifier for the source plugin.
     * This is the primary key and is auto-incremented.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The name of the plugin. Cannot be blank.
     */
    @Column(nullable = false, name = "plugin_name", length = 100)
    @NotBlank(message = "Plugin name cannot be blank.")
    private String pluginName;

    /**
     * A flag indicating whether the plugin is enabled.
     * Defaults to true.
     */
    @Column(nullable = false, name = "enabled")
    private Boolean enabled = true;

    /**
     * A description of the plugin. Optional.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * The timestamp when the record was created.
     * This value is set automatically on creation.
     */
    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor.
     */
    public SourcePlugin() {
        LOGGER.debug("SourcePlugin entity initialized with default constructor.");
    }

    /**
     * Constructor with parameters.
     *
     * @param pluginName the name of the plugin (must not be blank).
     * @param enabled whether the plugin is enabled (default: true).
     * @param description a description of the plugin.
     */
    public SourcePlugin(String pluginName, Boolean enabled, String description) {
        if (pluginName == null || pluginName.trim().isEmpty()) {
            LOGGER.error("Plugin name cannot be null or blank when creating SourcePlugin.");
            throw new IllegalArgumentException("Plugin name cannot be null or blank when creating SourcePlugin.");
        }
        this.pluginName = pluginName;
        this.enabled = enabled != null ? enabled : true;
        this.description = description;

        LOGGER.debug("SourcePlugin created with name '{}' and enabled '{}'.", pluginName, this.enabled);
    }

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        if (pluginName == null || pluginName.trim().isEmpty()) {
            LOGGER.error("Attempted to set a null or blank plugin name on SourcePlugin.");
            throw new IllegalArgumentException("Attempted to set a null or blank plugin name on SourcePlugin.");
        }
        this.pluginName = pluginName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled != null ? enabled : true;
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
        LOGGER.debug("SourcePlugin entity created at {}.", createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SourcePlugin that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SourcePlugin{" +
                "id=" + id +
                ", pluginName='" + pluginName + '\'' +
                ", enabled=" + enabled +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
