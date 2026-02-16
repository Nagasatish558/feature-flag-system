package com.featureflag.repository.api;

import java.util.List;
import java.util.Map;

/**
 * Interface for fast feature flag queries using NamedJdbc
 * Follows Dependency Inversion Principle (DIP)
 */
public interface IFeatureFlagQueryRepository {
    
    /**
     * Get all feature keys
     */
    List<String> getAllFeatureKeys();
    
    /**
     * Get user's groups efficiently
     */
    List<String> getUserGroups(String userId);
    
    /**
     * Check if user has override for a feature
     */
    boolean hasUserOverride(Long featureId, String userId);
    
    /**
     * Get user override state
     */
    Boolean getUserOverrideState(Long featureId, String userId);
    
    /**
     * Get group override states for a feature
     */
    Map<String, Boolean> getGroupOverrideStates(Long featureId, List<String> groupIds);
}
