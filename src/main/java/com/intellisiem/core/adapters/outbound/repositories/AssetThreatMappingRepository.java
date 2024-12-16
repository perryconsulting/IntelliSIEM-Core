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

import com.intellisiem.core.domain.models.AssetThreatMapping;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for performing CRUD operations on {@link AssetThreatMapping} entities.
 *
 * <p>This interface extends {@link CrudRepository} to provide CRUD operations
 * and additional query methods for the 'asset_threat_mapping' table in the 'intellisiem' schema.</p>
 */
@Repository
public interface AssetThreatMappingRepository extends CrudRepository<AssetThreatMapping, Integer> {

    /**
     * Retrieves all {@link AssetThreatMapping} entities associated with a specific asset.
     *
     * @param assetId the unique identifier of the associated asset.
     * @return a list of asset-threat mappings linked to the specified asset.
     */
    @Query("SELECT * FROM intellisiem.asset_threat_mapping WHERE asset_id = :assetId")
    List<AssetThreatMapping> findByAssetId(UUID assetId);

    /**
     * Retrieves all {@link AssetThreatMapping} entities associated with a specific threat.
     *
     * @param threatId the unique identifier of the associated threat.
     * @return a list of asset-threat mappings linked to the specified threat.
     */
    @Query("SELECT * FROM intellisiem.asset_threat_mapping WHERE threat_id = :threatId")
    List<AssetThreatMapping> findByThreatId(Integer threatId);

    /**
     * Retrieves all {@link AssetThreatMapping} entities with a relevance score greater than the specified threshold.
     *
     * @param threshold the relevance score threshold.
     * @return a list of asset-threat mappings with relevance scores exceeding the threshold.
     */
    @Query("SELECT * FROM intellisiem.asset_threat_mapping WHERE relevance_score > :threshold")
    List<AssetThreatMapping> findByRelevanceScoreGreaterThan(Double threshold);
}
