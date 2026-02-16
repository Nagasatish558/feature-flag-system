package com.featureflag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feature_group_overrides", uniqueConstraints = {
    @UniqueConstraint(name = "uk_feature_group", columnNames = {"feature_id", "group_id"})
}, indexes = {
    @Index(name = "idx_group_override", columnList = "feature_id,group_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureGroupOverride extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feature_id", nullable = false)
    private Long featureId;

    @Column(name = "group_id", nullable = false, length = 100)
    private String groupId;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
}
