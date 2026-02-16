package com.featureflag.service.api;

/**
 * Interface for managing feature flag overrides
 * Follows Single Responsibility Principle (SRP)
 */
public interface IOverrideService {
    /**
     * Create a user override
     * Enforces: Feature must exist, cannot create duplicate
     */
    void createUserOverride(Long featureId, String userId, Boolean enabled);
    
    /**
     * Delete a user override
     * Enforces: Override must exist
     */
    void deleteUserOverride(Long featureId, String userId);
    
    /**
     * Create a group override
     * Enforces: Feature must exist, cannot create duplicate
     */
    void createGroupOverride(Long featureId, String groupId, Boolean enabled);
    
    /**
     * Delete a group override
     * Enforces: Override must exist
     */
    void deleteGroupOverride(Long featureId, String groupId);
}
