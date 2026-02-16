package com.featureflag.service.impl;

import com.featureflag.exception.DuplicateOverrideException;
import com.featureflag.exception.FeatureFlagNotFoundException;
import com.featureflag.exception.OverrideNotFoundException;
import com.featureflag.entity.FeatureGroupOverride;
import com.featureflag.entity.FeatureUserOverride;
import com.featureflag.repository.api.IFeatureFlagRepository;
import com.featureflag.repository.api.IFeatureGroupOverrideRepository;
import com.featureflag.repository.api.IFeatureUserOverrideRepository;
import com.featureflag.service.api.IOverrideService;
import com.featureflag.utils.MessageConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OverrideService implements IOverrideService {

    private final IFeatureFlagRepository featureFlagRepository;
    private final IFeatureUserOverrideRepository userOverrideRepository;
    private final IFeatureGroupOverrideRepository groupOverrideRepository;

    @Override
    @CacheEvict(value = "evaluations", allEntries = true)
    public void createUserOverride(Long featureId, String userId, Boolean enabled) {
        log.info(MessageConstants.CREATING_USER_OVERRIDE, featureId, userId, enabled);

        // Enforce: Feature must exist
        if (!featureFlagRepository.existsById(featureId)) {
            throw new FeatureFlagNotFoundException(MessageConstants.FEATURE_NOT_FOUND + featureId);
        }

        // Enforce: Cannot insert duplicate override
        if (userOverrideRepository.existsByFeatureIdAndUserId(featureId, userId)) {
            throw new DuplicateOverrideException(MessageConstants.DUPLICATE_OVERRIDE + featureId + " and user: " + userId);
        }

        FeatureUserOverride override = FeatureUserOverride.builder()
            .featureId(featureId)
            .userId(userId)
            .enabled(enabled)
            .build();

        userOverrideRepository.save(override);
        log.info(MessageConstants.USER_OVERRIDE_CREATED);
    }

    @Override
    @CacheEvict(value = "evaluations", allEntries = true)
    public void deleteUserOverride(Long featureId, String userId) {
        log.info(MessageConstants.DELETING_USER_OVERRIDE, featureId, userId);

        // Enforce: Cannot delete non-existing override
        FeatureUserOverride override = userOverrideRepository.findByFeatureIdAndUserId(featureId, userId)
            .orElseThrow(() -> new OverrideNotFoundException(MessageConstants.OVERRIDE_NOT_FOUND + featureId + " and user: " + userId));

        userOverrideRepository.delete(override);
        log.info(MessageConstants.USER_OVERRIDE_DELETED);
    }

    @Override
    @CacheEvict(value = "evaluations", allEntries = true)
    public void createGroupOverride(Long featureId, String groupId, Boolean enabled) {
        log.info(MessageConstants.CREATING_GROUP_OVERRIDE, featureId, groupId, enabled);

        // Enforce: Feature must exist
        if (!featureFlagRepository.existsById(featureId)) {
            throw new FeatureFlagNotFoundException(MessageConstants.FEATURE_NOT_FOUND + featureId);
        }

        // Enforce: Cannot insert duplicate override
        if (groupOverrideRepository.existsByFeatureIdAndGroupId(featureId, groupId)) {
            throw new DuplicateOverrideException(MessageConstants.DUPLICATE_OVERRIDE + featureId + " and group: " + groupId);
        }

        FeatureGroupOverride override = FeatureGroupOverride.builder()
            .featureId(featureId)
            .groupId(groupId)
            .enabled(enabled)
            .build();

        groupOverrideRepository.save(override);
        log.info(MessageConstants.GROUP_OVERRIDE_CREATED);
    }

    @Override
    @CacheEvict(value = "evaluations", allEntries = true)
    public void deleteGroupOverride(Long featureId, String groupId) {
        log.info(MessageConstants.DELETING_GROUP_OVERRIDE, featureId, groupId);

        // Enforce: Cannot delete non-existing override
        FeatureGroupOverride override = groupOverrideRepository.findByFeatureIdAndGroupId(featureId, groupId)
            .orElseThrow(() -> new OverrideNotFoundException(MessageConstants.OVERRIDE_NOT_FOUND + featureId + " and group: " + groupId));

        groupOverrideRepository.delete(override);
        log.info(MessageConstants.GROUP_OVERRIDE_DELETED);
    }
}
