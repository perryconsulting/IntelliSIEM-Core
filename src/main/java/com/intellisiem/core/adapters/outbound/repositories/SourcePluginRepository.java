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

package com.intellisiem.core.adapters.outbound.repositories;

import com.intellisiem.core.domain.models.SourcePlugin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link SourcePlugin} entities.
 * This interface provides both basic CRUD functionality and custom query methods.
 */
@Repository
public interface SourcePluginRepository extends JpaRepository<SourcePlugin, Integer> {

    /**
     * Retrieves all enabled {@link SourcePlugin} entities.
     *
     * @return a list of source plugins where the "enabled" flag is true.
     */
    List<SourcePlugin> findByEnabledTrue();

    /**
     * Finds a {@link SourcePlugin} entity by its plugin name.
     *
     * @param pluginName the name of the plugin to search for.
     * @return an {@link Optional} containing the matching source plugin, if found.
     */
    @Query("SELECT sp FROM SourcePlugin sp WHERE sp.pluginName = :pluginName")
    Optional<SourcePlugin> findByPluginName(@Param("pluginName") String pluginName);
}
