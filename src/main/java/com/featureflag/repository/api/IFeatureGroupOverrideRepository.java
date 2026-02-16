package com.featureflag.repository.api;

import com.featureflag.entity.FeatureGroupOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Interface for FeatureGroupOverride persistence operations
 * Follows Dependency Inversion Principle (DIP)
 */
@Repository
public interface IFeatureGroupOverrideRepository extends JpaRepository<FeatureGroupOverride, Long> {
    Optional<FeatureGroupOverride> findByFeatureIdAndGroupId(Long featureId, String groupId);
    List<FeatureGroupOverride> findByFeatureId(Long featureId);
    boolean existsByFeatureIdAndGroupId(Long featureId, String groupId);
}
