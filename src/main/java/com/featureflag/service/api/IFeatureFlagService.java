package com.featureflag.service.api;

import com.featureflag.dto.FeatureFlagDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * Interface for managing feature flags
 * Follows Single Responsibility Principle (SRP)
 * Uses in-memory caching for scalable flag lookups
 */
public interface IFeatureFlagService {
    FeatureFlagDTO createFlag(String featureKey, String description, Boolean defaultEnabled);
    
    @Cacheable(value = "flags", key = "#id")
    FeatureFlagDTO getFlag(Long id);
    
    @Cacheable(value = "flags", key = "#featureKey")
    FeatureFlagDTO getFlagByKey(String featureKey);
    
    @CacheEvict(value = "flags", allEntries = true)
    FeatureFlagDTO updateFlag(Long id, String description, Boolean defaultEnabled);
    
    @CacheEvict(value = "flags", allEntries = true)
    void deleteFlag(Long id);
}
