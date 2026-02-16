package com.featureflag.repository.api;

import com.featureflag.entity.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface for FeatureFlag persistence operations
 * Follows Dependency Inversion Principle (DIP)
 */
@Repository
public interface IFeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
    Optional<FeatureFlag> findByFeatureKey(String featureKey);
    boolean existsByFeatureKey(String featureKey);
}
