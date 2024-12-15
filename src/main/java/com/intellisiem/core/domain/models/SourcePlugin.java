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
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Represents a source plugin in the IntelliSIEM system.
 *
 * <p>This class is mapped to the 'source_plugin' table in the database.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "intellisiem", name = "source_plugin")
@ToString(onlyExplicitlyIncluded = true)
public class SourcePlugin {

    /**
     * The unique identifier for the source plugin.
     * This is the primary key and is auto-incremented.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Integer id;

    /**
     * The name of the plugin. Cannot be blank.
     */
    @Column(nullable = false, name = "plugin_name", length = 100)
    @NotBlank(message = "Plugin name cannot be blank.")
    @ToString.Include
    private String pluginName;

    /**
     * A flag indicating whether the plugin is enabled.
     * Defaults to true.
     */
    @Column(nullable = false, name = "enabled")
    @ToString.Include
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
     * Lifecycle hook to set the creation timestamp before the entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
