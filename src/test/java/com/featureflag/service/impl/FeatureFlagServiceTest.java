package com.featureflag.service.impl;

import com.featureflag.dto.FeatureFlagDTO;
import com.featureflag.entity.FeatureFlag;
import com.featureflag.exception.FeatureFlagNotFoundException;
import com.featureflag.repository.api.IFeatureFlagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FeatureFlagService Tests")
class FeatureFlagServiceTest {

    private static final String FEATURE_KEY = "feature-test";
    private static final String NON_EXISTENT_KEY = "non-existent";
    private static final String UPDATED_DESCRIPTION = "Updated description";

    @Mock
    private IFeatureFlagRepository repository;

    @InjectMocks
    private FeatureFlagService service;

    private FeatureFlag testFlag;

    @BeforeEach
    void setUp() {
        testFlag = FeatureFlag.builder()
            .id(1L)
            .featureKey(FEATURE_KEY)
            .description("Test feature")
            .defaultEnabled(true)
            .build();
        testFlag.setCreatedAt(LocalDateTime.now());
        testFlag.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create a feature flag successfully")
    void testCreateFlag() {
        when(repository.save(any(FeatureFlag.class))).thenReturn(testFlag);

        FeatureFlagDTO result = service.createFlag(FEATURE_KEY, "Test feature", true);

        assertNotNull(result);
        assertEquals(FEATURE_KEY, result.getFeatureKey());
        assertEquals(true, result.getDefaultEnabled());
        verify(repository, times(1)).save(any(FeatureFlag.class));
    }

    @Test
    @DisplayName("Should get feature flag by ID successfully")
    void testGetFlag() {
        when(repository.findById(1L)).thenReturn(Optional.of(testFlag));

        FeatureFlagDTO result = service.getFlag(1L);

        assertNotNull(result);
        assertEquals(FEATURE_KEY, result.getFeatureKey());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when feature flag not found by ID")
    void testGetFlagNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(FeatureFlagNotFoundException.class, () -> service.getFlag(999L));
        verify(repository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get feature flag by key successfully")
    void testGetFlagByKey() {
        when(repository.findByFeatureKey(FEATURE_KEY)).thenReturn(Optional.of(testFlag));

        FeatureFlagDTO result = service.getFlagByKey(FEATURE_KEY);

        assertNotNull(result);
        assertEquals(FEATURE_KEY, result.getFeatureKey());
        verify(repository, times(1)).findByFeatureKey(FEATURE_KEY);
    }

    @Test
    @DisplayName("Should throw exception when feature flag not found by key")
    void testGetFlagByKeyNotFound() {
        when(repository.findByFeatureKey(NON_EXISTENT_KEY)).thenReturn(Optional.empty());

        assertThrows(FeatureFlagNotFoundException.class, () -> service.getFlagByKey(NON_EXISTENT_KEY));
        verify(repository, times(1)).findByFeatureKey(NON_EXISTENT_KEY);
    }

    @Test
    @DisplayName("Should update feature flag successfully")
    void testUpdateFlag() {
        FeatureFlag updatedFlag = FeatureFlag.builder()
            .id(1L)
            .featureKey(FEATURE_KEY)
            .description(UPDATED_DESCRIPTION)
            .defaultEnabled(false)
            .build();
        updatedFlag.setCreatedAt(LocalDateTime.now());
        updatedFlag.setUpdatedAt(LocalDateTime.now());

        when(repository.findById(1L)).thenReturn(Optional.of(testFlag));
        when(repository.save(any(FeatureFlag.class))).thenReturn(updatedFlag);

        FeatureFlagDTO result = service.updateFlag(1L, UPDATED_DESCRIPTION, false);

        assertNotNull(result);
        assertEquals(UPDATED_DESCRIPTION, result.getDescription());
        assertEquals(false, result.getDefaultEnabled());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(FeatureFlag.class));
    }

    @Test
    @DisplayName("Should delete feature flag successfully")
    void testDeleteFlag() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteFlag(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent feature flag")
    void testDeleteFlagNotFound() {
        when(repository.existsById(999L)).thenReturn(false);

        assertThrows(FeatureFlagNotFoundException.class, () -> service.deleteFlag(999L));
        verify(repository, times(1)).existsById(999L);
        verify(repository, never()).deleteById(anyLong());
    }
}
