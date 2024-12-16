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

import com.intellisiem.core.domain.models.AffectedProduct;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link AffectedProduct} entities.
 *
 * <p>This interface extends {@link CrudRepository} to provide CRUD operations
 * and additional query methods for the 'affected_product' table.</p>
 */
@Repository
public interface AffectedProductRepository extends CrudRepository<AffectedProduct, Integer> {

    /**
     * Retrieves all affected products associated with a specific vulnerability ID.
     *
     * @param vulnerabilityId the unique identifier of the associated vulnerability.
     * @return a list of affected products linked to the specified vulnerability.
     */
    @Query("SELECT * FROM intellisiem.affected_product WHERE vulnerability_id = :vulnerabilityId")
    List<AffectedProduct> findByVulnerabilityId(Integer vulnerabilityId);

    /**
     * Checks whether an affected product exists for a specific vulnerability ID
     * and product name.
     *
     * @param vulnerabilityId the unique identifier of the associated vulnerability.
     * @param productName the name of the affected product.
     * @return true if an affected product with the given parameters exists, false otherwise.
     */
    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
            "FROM intellisiem.affected_product WHERE vulnerability_id = :vulnerabilityId " +
            "AND product_name = :productName")
    boolean existsByVulnerabilityIdAndProductName(Integer vulnerabilityId, String productName);

    /**
     * Deletes all affected products associated with a specific vulnerability ID.
     *
     * @param vulnerabilityId the unique identifier of the associated vulnerability.
     */
    @Modifying
    @Query("DELETE FROM intellisiem.affected_product WHERE vulnerability_id = :vulnerabilityId")
    void deleteByVulnerabilityId(Integer vulnerabilityId);
}
