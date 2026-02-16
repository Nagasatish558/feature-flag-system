package com.featureflag.service.impl;

import com.featureflag.dto.FeatureEvaluationDTO;
import com.featureflag.entity.FeatureFlag;
import com.featureflag.exception.FeatureFlagNotFoundException;
import com.featureflag.repository.api.IFeatureFlagQueryRepository;
import com.featureflag.repository.api.IFeatureFlagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FeatureEvaluationService Tests")
class FeatureEvaluationServiceTest {

    private static final String FEATURE_KEY = "feature-test";
    private static final String USER_ID = "user123";
    private static final String ADMIN_GROUP = "admin";
    private static final String DEV_GROUP = "dev";

    @Mock
    private IFeatureFlagRepository repository;

    @Mock
    private IFeatureFlagQueryRepository queryRepository;

    @InjectMocks
    private FeatureEvaluationService service;

    private FeatureFlag testFlag;

    @BeforeEach
    void setUp() {
        testFlag = FeatureFlag.builder()
            .id(1L)
            .featureKey(FEATURE_KEY)
            .defaultEnabled(true)
            .build();
        testFlag.setCreatedAt(LocalDateTime.now());
        testFlag.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should return user override when it exists")
    void testEvaluateFeatureWithUserOverride() {
        when(repository.findByFeatureKey(FEATURE_KEY)).thenReturn(Optional.of(testFlag));
        when(queryRepository.getUserOverrideState(1L, USER_ID)).thenReturn(false);

        FeatureEvaluationDTO result = service.evaluateFeature(FEATURE_KEY, USER_ID);

        assertNotNull(result);
        assertEquals(false, result.getEnabled());
        assertEquals("USER_OVERRIDE", result.getReason());
        verify(repository, times(1)).findByFeatureKey(FEATURE_KEY);
        verify(queryRepository, times(1)).getUserOverrideState(1L, USER_ID);
        verify(queryRepository, never()).getUserGroups(anyString());
    }

    @Test
    @DisplayName("Should return group override when no user override exists")
    void testEvaluateFeatureWithGroupOverride() {
        when(repository.findByFeatureKey(FEATURE_KEY)).thenReturn(Optional.of(testFlag));
        when(queryRepository.getUserOverrideState(1L, USER_ID)).thenReturn(null);
        when(queryRepository.getUserGroups(USER_ID)).thenReturn(List.of(ADMIN_GROUP, DEV_GROUP));

        Map<String, Boolean> groupOverrides = new HashMap<>();
        groupOverrides.put(ADMIN_GROUP, false);
        when(queryRepository.getGroupOverrideStates(1L, List.of(ADMIN_GROUP, DEV_GROUP))).thenReturn(groupOverrides);

        FeatureEvaluationDTO result = service.evaluateFeature(FEATURE_KEY, USER_ID);

        assertNotNull(result);
        assertEquals(false, result.getEnabled());
        assertEquals("GROUP_OVERRIDE", result.getReason());
    }

    @Test
    @DisplayName("Should return default state when no overrides exist")
    void testEvaluateFeatureWithDefault() {
        when(repository.findByFeatureKey(FEATURE_KEY)).thenReturn(Optional.of(testFlag));
        when(queryRepository.getUserOverrideState(1L, USER_ID)).thenReturn(null);
        when(queryRepository.getUserGroups(USER_ID)).thenReturn(List.of());

        FeatureEvaluationDTO result = service.evaluateFeature(FEATURE_KEY, USER_ID);

        assertNotNull(result);
        assertEquals(true, result.getEnabled());
        assertEquals("DEFAULT", result.getReason());
    }

    @Test
    @DisplayName("Should throw exception for non-existent feature")
    void testEvaluateFeatureNotFound() {
        when(repository.findByFeatureKey("non-existent")).thenReturn(Optional.empty());

        assertThrows(FeatureFlagNotFoundException.class, () -> service.evaluateFeature("non-existent", USER_ID));
    }

    @Test
    @DisplayName("Should get default state successfully")
    void testGetDefaultState() {
        when(repository.findByFeatureKey(FEATURE_KEY)).thenReturn(Optional.of(testFlag));

        Boolean result = service.getDefaultState(FEATURE_KEY);

        assertTrue(result);
        verify(repository, times(1)).findByFeatureKey(FEATURE_KEY);
    }
}
