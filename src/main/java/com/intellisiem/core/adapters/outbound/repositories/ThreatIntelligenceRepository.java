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

import com.intellisiem.core.domain.models.ThreatIntelligence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on {@link ThreatIntelligence} entities.
 * This interface provides both basic CRUD functionality and custom query methods.
 */
@Repository
public interface ThreatIntelligenceRepository extends JpaRepository<ThreatIntelligence, Integer> {

    /**
     * Retrieves all {@link ThreatIntelligence} entities with the specified severity level.
     *
     * @param severity the severity level (e.g., "critical", "high", "medium", "low").
     * @return a list of threat intelligence records matching the specified severity.
     */
    List<ThreatIntelligence> findBySeverity(String severity);

    /**
     * Retrieves all {@link ThreatIntelligence} entities of a specific threat type.
     *
     * @param threatType the type of threat (e.g., "Malware", "CVE", "IP", "Domain").
     * @return a list of threat intelligence records matching the specified type.
     */
    List<ThreatIntelligence> findByThreatType(String threatType);

    /**
     * Searches for {@link ThreatIntelligence} entities where the value matches the provided pattern.
     *
     * @param value the search pattern (e.g., partial match for threat values).
     * @return a list of threat intelligence records whose values match the pattern.
     */
    @Query("SELECT ti FROM ThreatIntelligence ti WHERE ti.value LIKE %:value%")
    List<ThreatIntelligence> searchByValue(@Param("value") String value);
}
