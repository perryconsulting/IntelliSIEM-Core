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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link AssetSource} entities.
 * This interface provides both basic CRUD functionality and custom query methods.
 */
@Repository
public interface AssetSourceRepository extends JpaRepository<AssetSource, Integer> {

    /**
     * Finds an {@link AssetSource} by its name.
     *
     * @param name the name of the asset source to retrieve.
     * @return an {@link Optional} containing the matching asset source, if found.
     */
    Optional<AssetSource> findByName(String name);
}
