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

-- Seed data for asset_source
INSERT INTO asset_source (id, name, description)
VALUES (1, 'Nmap', 'Network Mapper'),
       (2, 'Nessus', 'Vulnerability Scanner'),
       (3, 'Manual', 'Manually Added Assets');

-- Seed data for asset
INSERT INTO asset (id, hostname, fqdn, mac_address, asset_type, os_name, os_version, criticality, source_id, created_at,
                   updated_at)
VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'web-server-01', 'web01.example.com', '00:1A:2B:3C:4D:5E', 'Server',
        'Ubuntu', '20.04', 'high', 1, NOW(), NOW()),
       ('d13ac22c-59bb-4872-a561-1f22c4c1a572', 'db-server-01', 'db01.example.com', '00:1A:2B:3C:4D:5F', 'Database',
        'CentOS', '7.9', 'medium', 2, NOW(), NOW()),
       ('d23ac10a-58cc-4372-a567-0e02b2c3d479', 'test-laptop-01', 'laptop01.local', '00:2B:3C:4D:5E:6F', 'Laptop',
        'Windows 10', '21H2', 'low', 3, NOW(), NOW());

-- Seed data for ip_address
INSERT INTO ip_address (id, asset_id, ip_address, created_at)
VALUES (1, 'f47ac10b-58cc-4372-a567-0e02b2c3d479', '192.168.1.10', NOW()),
       (2, 'd13ac22c-59bb-4872-a561-1f22c4c1a572', '192.168.1.20', NOW()),
       (3, 'd23ac10a-58cc-4372-a567-0e02b2c3d479', '192.168.1.30', NOW());

-- Seed data for threat_intelligence
INSERT INTO threat_intelligence (id, threat_type, value, description, severity, first_seen, last_seen, created_at)
VALUES (1, 'IP', '192.168.1.100', 'Known malicious IP address', 'high', NOW() - INTERVAL '10 days',
        NOW() - INTERVAL '5 days', NOW()),
       (2, 'Domain', 'malicious.example.com', 'Suspicious domain reported in threat feed', 'medium',
        NOW() - INTERVAL '20 days', NOW() - INTERVAL '15 days', NOW()),
       (3, 'CVE', 'CVE-2023-12345', 'Critical vulnerability affecting servers', 'critical', NOW() - INTERVAL '30 days',
        NOW() - INTERVAL '1 day', NOW());

-- Seed data for vulnerability
INSERT INTO vulnerability (id, cve_id, description, severity, exploit_available, created_at)
VALUES (1, 'CVE-2023-12345', 'Remote code execution in vulnerable server', 'critical', TRUE, NOW()),
       (2, 'CVE-2023-54321', 'Privilege escalation in outdated software', 'high', FALSE, NOW());

-- Seed data for affected_product
INSERT INTO affected_product (id, vulnerability_id, product_name, created_at)
VALUES (1, 1, 'Ubuntu 20.04 LTS', NOW()),
       (2, 2, 'Windows 10 Pro 21H2', NOW());

-- Seed data for asset_threat_mapping
INSERT INTO asset_threat_mapping (id, asset_id, threat_id, relevance_score, created_at)
VALUES (1, 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 1, 90.50, NOW()),
       (2, 'd13ac22c-59bb-4872-a561-1f22c4c1a572', 3, 85.75, NOW());

-- Seed data for source_plugin
INSERT INTO source_plugin (id, plugin_name, enabled, description, created_at)
VALUES (1, 'Nmap Plugin', TRUE, 'Plugin to parse Nmap XML files', NOW()),
       (2, 'Nessus Plugin', TRUE, 'Plugin to parse Nessus XML files', NOW()),
       (3, 'CSV Import Plugin', FALSE, 'Plugin to import assets from CSV files', NOW());
