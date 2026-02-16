package com.featureflag.service.api;

import com.featureflag.dto.FeatureEvaluationDTO;

/**
 * Interface for evaluating feature flags
 * Core evaluation logic with user and group overrides
 * User overrides have precedence over group overrides
 */
public interface IFeatureEvaluationService {
    /**
     * Evaluate if a feature is enabled for a user
     * Priority: User Override > Group Override > Default
     * 
     * @param featureKey unique feature identifier
     * @param userId unique user identifier
     * @return evaluation result with enabled state and reason
     */
    FeatureEvaluationDTO evaluateFeature(String featureKey, String userId);
    
    /**
     * Get default state of a feature
     */
    Boolean getDefaultState(String featureKey);
}
