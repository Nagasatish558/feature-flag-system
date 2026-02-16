package com.featureflag.repository.api;

import com.featureflag.entity.FeatureUserOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Interface for FeatureUserOverride persistence operations
 * Follows Dependency Inversion Principle (DIP)
 */
@Repository
public interface IFeatureUserOverrideRepository extends JpaRepository<FeatureUserOverride, Long> {
    Optional<FeatureUserOverride> findByFeatureIdAndUserId(Long featureId, String userId);
    boolean existsByFeatureIdAndUserId(Long featureId, String userId);
}
