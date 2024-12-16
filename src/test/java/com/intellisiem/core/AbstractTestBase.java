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

package com.intellisiem.core;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Objects;
import java.util.Set;

/**
 * Base class for all integration and application-level tests in IntelliSIEM-Core.
 *
 * <p>This class ensures a clean and consistent database state before each test
 * and provides shared test setup functionality for all derived test classes.</p>
 */
@SpringBootTest
@ActiveProfiles("test") // Ensures that the "test" profile is active for all tests.
public abstract class AbstractTestBase {

    @Autowired
    private Flyway flyway;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Set<String> ALLOWED_TABLES = Set.of(
            "asset",
            "asset_source",
            "ip_address",
            "threat_intelligence",
            "vulnerability",
            "affected_product",
            "asset_threat_mapping",
            "source_plugin"
    );

    /**
     * Runs before each test to clean and reset the database.
     * This ensures no state leakage between tests.
     */
    @BeforeEach
    public void resetDatabase() {
        flyway.clean();   // Cleans the database schema
        flyway.migrate(); // Reapplies all migrations
        System.out.println("Database reset: Cleaned and migrated successfully.");
    }

    /**
     * Optionally, runs once per test class to seed shared test data.
     * Override this method in derived test classes to provide specific seed logic.
     */
    @BeforeAll
    public static void seedTestData() {
        System.out.println("Seed test data if applicable.");
    }

    /**
     * Truncates specific tables in the database if they are valid.
     *
     * @param tableNames the names of the tables to truncate.
     */
    protected void truncateTables(String... tableNames) {
        for (String table : tableNames) {
            if (isTableNameValid(table)) {
                String sql = "TRUNCATE TABLE " + table + " CASCADE";
                jdbcTemplate.execute(sql);
                System.out.println("Truncated table: " + table);
            } else {
                System.err.println("Invalid table name skipped: " + table);
            }
        }
    }

    /**
     * Counts the number of rows in a specific table.
     *
     * @param tableName the name of the table to query.
     * @return the number of rows in the specified table.
     */
    protected int countRowsInTable(String tableName) {
        if (!isTableNameValid(tableName)) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            System.err.println("Error querying table: " + tableName + ". Exception: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Validates if the given table name is allowed for operations.
     *
     * @param tableName the name of the table to validate.
     * @return true if the table name is valid; false otherwise.
     */
    private boolean isTableNameValid(String tableName) {
        return ALLOWED_TABLES.contains(tableName);
    }
}
