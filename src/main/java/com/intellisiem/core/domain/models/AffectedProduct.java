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

package com.intellisiem.core.domain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/**
 * Represents a product affected by a vulnerability in the IntelliSIEM system.
 *
 * <p>This class is mapped to the 'affected_product' table in the database.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "intellisiem", name = "affected_product")
@ToString(onlyExplicitlyIncluded = true)
public class AffectedProduct {

    /**
     * The unique identifier for the affected product record.
     * This is the primary key and is auto-incremented.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Integer id;

    /**
     * The associated vulnerability to which this product is related.
     * This field establishes a many-to-one relationship with the {@link Vulnerability} entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vulnerability_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // Ensures cascading deletes at the database level
    @ToString.Exclude
    private Vulnerability vulnerability;

    /**
     * The name of the affected product.
     * Cannot be blank.
     */
    @Column(nullable = false, name = "product_name")
    @NotBlank(message = "Product name cannot be blank.")
    @ToString.Include
    private String productName;

    /**
     * The timestamp when the record was created.
     * This value is set automatically on creation.
     */
    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Lifecycle hook to set the creation timestamp before the entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
