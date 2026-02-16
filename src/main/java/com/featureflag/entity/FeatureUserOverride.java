package com.featureflag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feature_user_overrides", uniqueConstraints = {
    @UniqueConstraint(name = "uk_feature_user", columnNames = {"feature_id", "user_id"})
}, indexes = {
    @Index(name = "idx_user_override", columnList = "feature_id,user_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureUserOverride extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feature_id", nullable = false)
    private Long featureId;

    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
}
