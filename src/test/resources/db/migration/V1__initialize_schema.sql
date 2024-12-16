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

-- Table: asset_source
CREATE TABLE asset_source (
                              id SERIAL PRIMARY KEY,
                              name VARCHAR(255) NOT NULL UNIQUE,
                              description TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: asset
CREATE TABLE asset (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       hostname VARCHAR(255) NOT NULL UNIQUE,
                       fqdn VARCHAR(255),
                       mac_address VARCHAR(17),
                       asset_type VARCHAR(50),
                       os_name VARCHAR(100),
                       os_version VARCHAR(50),
                       criticality VARCHAR(10) CHECK (criticality IN ('high', 'medium', 'low')),
                       source_id INT REFERENCES asset_source(id) ON DELETE SET NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: ip_address (normalized from asset.ip_addresses)
CREATE TABLE ip_address (
                            id SERIAL PRIMARY KEY,
                            asset_id UUID REFERENCES asset(id) ON DELETE CASCADE,
                            ip_address VARCHAR(39) NOT NULL, -- Supports IPv4 and IPv6
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: threat_intelligence
CREATE TABLE threat_intelligence (
                                     id SERIAL PRIMARY KEY,
                                     threat_type VARCHAR(50) NOT NULL, -- e.g., "Malware", "CVE", "IP", "Domain"
                                     value TEXT NOT NULL,
                                     description TEXT,
                                     severity VARCHAR(10) CHECK (severity IN ('critical', 'high', 'medium', 'low')),
                                     first_seen TIMESTAMP,
                                     last_seen TIMESTAMP,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: vulnerability
CREATE TABLE vulnerability (
                               id SERIAL PRIMARY KEY,
                               cve_id VARCHAR(20) UNIQUE,
                               description TEXT,
                               severity VARCHAR(10) CHECK (severity IN ('critical', 'high', 'medium', 'low')),
                               exploit_available BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: affected_product (normalized from vulnerability.affected_products)
CREATE TABLE affected_product (
                                  id SERIAL PRIMARY KEY,
                                  vulnerability_id INT REFERENCES vulnerability(id) ON DELETE CASCADE,
                                  product_name VARCHAR(255) NOT NULL,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: asset_threat_mapping
CREATE TABLE asset_threat_mapping (
                                      id SERIAL PRIMARY KEY,
                                      asset_id UUID REFERENCES asset(id) ON DELETE CASCADE,
                                      threat_id INT REFERENCES threat_intelligence(id) ON DELETE CASCADE,
                                      relevance_score DECIMAL(5, 2) NOT NULL DEFAULT 0.0, -- Calculated score for prioritization
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: source_plugin
CREATE TABLE source_plugin (
                               id SERIAL PRIMARY KEY,
                               plugin_name VARCHAR(100) NOT NULL,
                               enabled BOOLEAN DEFAULT TRUE,
                               description TEXT,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_asset_hostname ON asset (hostname);
CREATE INDEX idx_asset_source_id ON asset (source_id);
CREATE INDEX idx_threat_type ON threat_intelligence (threat_type);
CREATE INDEX idx_threat_value ON threat_intelligence (value);
CREATE INDEX idx_ip_address ON ip_address (ip_address);
