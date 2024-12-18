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

package com.intellisiem.core.application.services;

import com.intellisiem.core.AbstractTestBase;
import com.intellisiem.core.adapters.outbound.repositories.AssetRepository;
import com.intellisiem.core.domain.enums.Criticality;
import com.intellisiem.core.domain.models.Asset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetServiceTest extends AbstractTestBase {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetService assetService;

    private Asset testAsset;

    @BeforeEach
    void setUp() {
        super.resetDatabase(); // Ensures clean DB before each test

        testAsset = new Asset(
                "test-host",
                "test.fqdn.com",
                "00:1A:2B:3C:4D:5E",
                "Server",
                "Ubuntu",
                "20.04",
                Criticality.HIGH,
                null
        );
        testAsset.setId(UUID.randomUUID());
    }

    @Test
    void testCreateAsset() {
        when(assetRepository.save(any(Asset.class))).thenReturn(testAsset);

        Asset createdAsset = assetService.createAsset(testAsset);

        assertNotNull(createdAsset);
        assertEquals("test-host", createdAsset.getHostname());
        verify(assetRepository, times(1)).save(testAsset);
    }

    @Test
    void testGetAssetById() {
        UUID assetId = testAsset.getId();
        when(assetRepository.findById(assetId)).thenReturn(Optional.of(testAsset));

        Asset foundAsset = assetService.getAssetById(assetId);

        assertNotNull(foundAsset);
        assertEquals("test-host", foundAsset.getHostname());
        verify(assetRepository, times(1)).findById(assetId);
    }

    @Test
    void testTruncateTables() {
        truncateTables("asset", "asset_source"); // Directly call inherited method
        int rowCount = countRowsInTable("asset");

        assertEquals(0, rowCount, "Table 'asset' should be empty after truncation.");
    }
}
