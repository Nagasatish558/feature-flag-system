package com.featureflag.service.impl;

import com.featureflag.dto.FeatureFlagDTO;
import com.featureflag.entity.FeatureFlag;
import com.featureflag.exception.FeatureFlagNotFoundException;
import com.featureflag.repository.api.IFeatureFlagRepository;
import com.featureflag.service.api.IFeatureFlagService;
import com.featureflag.utils.MessageConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FeatureFlagService implements IFeatureFlagService {

    private final IFeatureFlagRepository featureFlagRepository;

    @Override
    public FeatureFlagDTO createFlag(String featureKey, String description, Boolean defaultEnabled) {
        log.info(MessageConstants.CREATING_FLAG, featureKey);
        
        FeatureFlag flag = FeatureFlag.builder()
            .featureKey(featureKey)
            .description(description)
            .defaultEnabled(defaultEnabled)
            .build();
        
        FeatureFlag saved = featureFlagRepository.save(flag);
        log.info(MessageConstants.FLAG_CREATED, saved.getId());
        
        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FeatureFlagDTO getFlag(Long id) {
        log.debug(MessageConstants.GETTING_FLAG, id);
        FeatureFlag flag = featureFlagRepository.findById(id)
            .orElseThrow(() -> new FeatureFlagNotFoundException(MessageConstants.FEATURE_NOT_FOUND + id));
        return mapToDTO(flag);
    }

    @Override
    @Transactional(readOnly = true)
    public FeatureFlagDTO getFlagByKey(String featureKey) {
        log.debug(MessageConstants.GETTING_FLAG_BY_KEY, featureKey);
        FeatureFlag flag = featureFlagRepository.findByFeatureKey(featureKey)
            .orElseThrow(() -> new FeatureFlagNotFoundException(MessageConstants.FEATURE_BY_KEY_NOT_FOUND + featureKey));
        return mapToDTO(flag);
    }

    @Override
    public FeatureFlagDTO updateFlag(Long id, String description, Boolean defaultEnabled) {
        log.info(MessageConstants.UPDATING_FLAG, id);
        
        FeatureFlag flag = featureFlagRepository.findById(id)
            .orElseThrow(() -> new FeatureFlagNotFoundException(MessageConstants.FEATURE_NOT_FOUND + id));
        
        flag.setDescription(description);
        flag.setDefaultEnabled(defaultEnabled);
        
        FeatureFlag updated = featureFlagRepository.save(flag);
        log.info(MessageConstants.FLAG_UPDATED, id);
        
        return mapToDTO(updated);
    }

    @Override
    public void deleteFlag(Long id) {
        log.info(MessageConstants.DELETING_FLAG, id);
        
        if (!featureFlagRepository.existsById(id)) {
            throw new FeatureFlagNotFoundException(MessageConstants.FEATURE_NOT_FOUND + id);
        }
        
        featureFlagRepository.deleteById(id);
        log.info(MessageConstants.FLAG_DELETED, id);
    }

    private FeatureFlagDTO mapToDTO(FeatureFlag flag) {
        return FeatureFlagDTO.builder()
            .id(flag.getId())
            .featureKey(flag.getFeatureKey())
            .description(flag.getDescription())
            .defaultEnabled(flag.getDefaultEnabled())
            .createdAt(flag.getCreatedAt())
            .updatedAt(flag.getUpdatedAt())
            .build();
    }
}
