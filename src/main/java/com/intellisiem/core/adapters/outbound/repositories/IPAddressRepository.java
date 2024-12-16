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

import com.intellisiem.core.domain.models.IPAddress;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for performing CRUD operations on {@link IPAddress} entities.
 *
 * <p>This interface extends {@link CrudRepository} to provide basic CRUD operations
 * and additional query methods for the 'ip_address' table in the 'intellisiem' schema.</p>
 */
@Repository
public interface IPAddressRepository extends CrudRepository<IPAddress, Integer> {

    /**
     * Retrieves all {@link IPAddress} entities associated with a specific asset.
     *
     * @param assetId the unique identifier of the associated asset.
     * @return a list of IP addresses linked to the specified asset.
     */
    @Query("SELECT * FROM intellisiem.ip_address WHERE asset_id = :assetId")
    List<IPAddress> findByAssetId(UUID assetId);

    /**
     * Finds an {@link IPAddress} by its IP address value.
     *
     * @param ip the IP address to retrieve.
     * @return an {@link Optional} containing the matching IP address, if found.
     */
    @Query("SELECT * FROM intellisiem.ip_address WHERE ip_address = :ip")
    Optional<IPAddress> findByIp(String ip);
}
