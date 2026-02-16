package com.featureflag.service.impl;

import com.featureflag.dto.FeatureEvaluationDTO;
import com.featureflag.entity.FeatureFlag;
import com.featureflag.exception.FeatureFlagNotFoundException;
import com.featureflag.repository.api.IFeatureFlagQueryRepository;
import com.featureflag.repository.api.IFeatureFlagRepository;
import com.featureflag.service.api.IFeatureEvaluationService;
import com.featureflag.utils.MessageConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeatureEvaluationService implements IFeatureEvaluationService {

    private final IFeatureFlagRepository featureFlagRepository;
    private final IFeatureFlagQueryRepository queryRepository;

    @Override
    public FeatureEvaluationDTO evaluateFeature(String featureKey, String userId) {
        log.debug(MessageConstants.EVALUATING_FEATURE, featureKey, userId);

        // Step 1: Get feature flag
        FeatureFlag flag = featureFlagRepository.findByFeatureKey(featureKey)
            .orElseThrow(() -> new FeatureFlagNotFoundException(MessageConstants.FEATURE_BY_KEY_NOT_FOUND + featureKey));

        // Step 2: Check user override (highest priority)
        Boolean userOverride = queryRepository.getUserOverrideState(flag.getId(), userId);
        if (userOverride != null) {
            log.debug(MessageConstants.USER_OVERRIDE_FOUND, featureKey, userOverride);
            return FeatureEvaluationDTO.builder()
                .featureKey(featureKey)
                .userId(userId)
                .enabled(userOverride)
                .reason("USER_OVERRIDE")
                .build();
        }

        // Step 3: Check group overrides (second priority)
        List<String> userGroups = queryRepository.getUserGroups(userId);
        if (!userGroups.isEmpty()) {
            Map<String, Boolean> groupOverrides = queryRepository.getGroupOverrideStates(flag.getId(), userGroups);
            
            // Get the first matching group override (any override takes precedence)
            if (!groupOverrides.isEmpty()) {
                Boolean groupEnabled = groupOverrides.values().iterator().next();
                log.debug(MessageConstants.GROUP_OVERRIDE_FOUND, featureKey, userId, groupEnabled);
                return FeatureEvaluationDTO.builder()
                    .featureKey(featureKey)
                    .userId(userId)
                    .enabled(groupEnabled)
                    .reason("GROUP_OVERRIDE")
                    .build();
            }
        }

        // Step 4: Use default state
        Boolean defaultState = flag.getDefaultEnabled();
        log.debug(MessageConstants.USING_DEFAULT_STATE, featureKey, defaultState);
        return FeatureEvaluationDTO.builder()
            .featureKey(featureKey)
            .userId(userId)
            .enabled(defaultState)
            .reason("DEFAULT")
            .build();
    }

    @Override
    public Boolean getDefaultState(String featureKey) {
        FeatureFlag flag = featureFlagRepository.findByFeatureKey(featureKey)
            .orElseThrow(() -> new FeatureFlagNotFoundException(MessageConstants.FEATURE_BY_KEY_NOT_FOUND + featureKey));
        return flag.getDefaultEnabled();
    }
}
