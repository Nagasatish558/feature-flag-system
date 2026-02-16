package com.featureflag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feature_flags", indexes = {
    @Index(name = "idx_feature_key", columnList = "feature_key", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureFlag extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feature_key", nullable = false, length = 100, unique = true)
    private String featureKey;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "default_enabled", nullable = false)
    private Boolean defaultEnabled;
}
