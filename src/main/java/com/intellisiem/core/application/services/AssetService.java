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

import com.intellisiem.core.adapters.outbound.repositories.AssetRepository;
import com.intellisiem.core.domain.enums.Criticality;
import com.intellisiem.core.domain.models.Asset;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public Asset createAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    public Asset getAssetById(UUID assetId) {
        return assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found."));
    }

    public List<Asset> getAllAssets() {
        return (List<Asset>) assetRepository.findAll();
    }

    public Asset updateAsset(UUID assetId, Asset updatedAsset) {
        Asset existingAsset = getAssetById(assetId);
        updatedAsset.setId(existingAsset.getId());
        return assetRepository.save(updatedAsset);
    }

    public void deleteAsset(UUID assetId) {
        assetRepository.deleteById(assetId);
    }
}

