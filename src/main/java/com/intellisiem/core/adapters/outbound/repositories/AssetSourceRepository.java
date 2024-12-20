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

import com.intellisiem.core.domain.models.AssetSource;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link AssetSource} entities.
 *
 * <p>This repository interacts with the 'asset_source' table in the database
 * using Spring Data JDBC to abstract most query and data mapping logic.</p>
 *
 * <p><strong>Error Handling:</strong> Spring Data JDBC implicitly handles common
 * database exceptions and converts them into appropriate runtime exceptions.</p>
 *
 * <p><strong>Logging:</strong> Enable logging at the Spring Data JDBC level to debug queries if needed.</p>
 */
@Repository
public interface AssetSourceRepository extends CrudRepository<AssetSource, Integer> {

    /**
     * Finds an {@link AssetSource} by its name.
     *
     * @param name the name of the asset source to retrieve.
     * @return an {@link Optional} containing the matching asset source, if found.
     */
    @Query("SELECT * FROM asset_source WHERE name = :name")
    Optional<AssetSource> findByName(String name);

    /**
     * Finds all {@link AssetSource} entities containing the specified description.
     *
     * @param description the description to search for.
     * @return a list of matching {@link AssetSource} entities.
     */
    @Query("SELECT * FROM asset_source WHERE description ILIKE '%' || :description || '%'")
    List<AssetSource> findByDescriptionContaining(String description);

    /**
     * Counts all {@link AssetSource} entities in the database.
     *
     * @return the total number of {@link AssetSource} entities.
     */
    @Query("SELECT COUNT(*) FROM asset_source")
    long countAll();

    /**
     * Updates the description of an {@link AssetSource} with the specified ID.
     *
     * @param id the ID of the asset source to update.
     * @param description the new description.
     * @return the number of rows affected.
     */
    @Query("UPDATE asset_source SET description = :description WHERE id = :id")
    int updateDescriptionById(int id, String description);

    /**
     * Checks if an {@link AssetSource} with the specified name exists.
     *
     * @param name the name of the asset source.
     * @return true if an entity exists with the specified name, false otherwise.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM asset_source WHERE name = :name)")
    boolean existsByName(String name);

    /**
     * Deletes all {@link AssetSource} entities with the specified name.
     *
     * @param name the name of the asset sources to delete.
     * @return the number of rows affected.
     */
    @Query("DELETE FROM asset_source WHERE name = :name")
    int deleteByName(String name);
}
