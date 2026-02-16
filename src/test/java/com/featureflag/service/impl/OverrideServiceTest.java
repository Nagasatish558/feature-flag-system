package com.featureflag.service.impl;

import com.featureflag.exception.DuplicateOverrideException;
import com.featureflag.exception.FeatureFlagNotFoundException;
import com.featureflag.exception.OverrideNotFoundException;
import com.featureflag.entity.FeatureGroupOverride;
import com.featureflag.entity.FeatureUserOverride;
import com.featureflag.repository.api.IFeatureFlagRepository;
import com.featureflag.repository.api.IFeatureGroupOverrideRepository;
import com.featureflag.repository.api.IFeatureUserOverrideRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OverrideService Tests")
class OverrideServiceTest {

    private static final String USER_ID = "user123";
    private static final String GROUP_ID = "admin";

    @Mock
    private IFeatureFlagRepository featureFlagRepository;

    @Mock
    private IFeatureUserOverrideRepository userOverrideRepository;

    @Mock
    private IFeatureGroupOverrideRepository groupOverrideRepository;

    @InjectMocks
    private OverrideService service;

    @Test
    @DisplayName("Should create user override successfully")
    void testCreateUserOverrideSuccess() {
        when(featureFlagRepository.existsById(1L)).thenReturn(true);
        when(userOverrideRepository.existsByFeatureIdAndUserId(1L, USER_ID)).thenReturn(false);

        service.createUserOverride(1L, USER_ID, true);

        verify(featureFlagRepository, times(1)).existsById(1L);
        verify(userOverrideRepository, times(1)).existsByFeatureIdAndUserId(1L, USER_ID);
        verify(userOverrideRepository, times(1)).save(any(FeatureUserOverride.class));
    }

    @Test
    @DisplayName("Should throw exception when feature does not exist")
    void testCreateUserOverrideFeatureNotFound() {
        when(featureFlagRepository.existsById(999L)).thenReturn(false);

        assertThrows(FeatureFlagNotFoundException.class, () -> service.createUserOverride(999L, USER_ID, true));
        verify(featureFlagRepository, times(1)).existsById(999L);
        verify(userOverrideRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when duplicate user override")
    void testCreateUserOverrideDuplicate() {
        when(featureFlagRepository.existsById(1L)).thenReturn(true);
        when(userOverrideRepository.existsByFeatureIdAndUserId(1L, USER_ID)).thenReturn(true);

        assertThrows(DuplicateOverrideException.class, () -> service.createUserOverride(1L, USER_ID, true));
        verify(userOverrideRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete user override successfully")
    void testDeleteUserOverrideSuccess() {
        FeatureUserOverride override = FeatureUserOverride.builder()
            .id(1L)
            .featureId(1L)
            .userId(USER_ID)
            .enabled(true)
            .build();

        when(userOverrideRepository.findByFeatureIdAndUserId(1L, USER_ID)).thenReturn(Optional.of(override));

        service.deleteUserOverride(1L, USER_ID);

        verify(userOverrideRepository, times(1)).delete(override);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent user override")
    void testDeleteUserOverrideNotFound() {
        when(userOverrideRepository.findByFeatureIdAndUserId(1L, USER_ID)).thenReturn(Optional.empty());

        assertThrows(OverrideNotFoundException.class, () -> service.deleteUserOverride(1L, USER_ID));
        verify(userOverrideRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should create group override successfully")
    void testCreateGroupOverrideSuccess() {
        when(featureFlagRepository.existsById(1L)).thenReturn(true);
        when(groupOverrideRepository.existsByFeatureIdAndGroupId(1L, GROUP_ID)).thenReturn(false);

        service.createGroupOverride(1L, GROUP_ID, true);

        verify(featureFlagRepository, times(1)).existsById(1L);
        verify(groupOverrideRepository, times(1)).existsByFeatureIdAndGroupId(1L, GROUP_ID);
        verify(groupOverrideRepository, times(1)).save(any(FeatureGroupOverride.class));
    }

    @Test
    @DisplayName("Should delete group override successfully")
    void testDeleteGroupOverrideSuccess() {
        FeatureGroupOverride override = FeatureGroupOverride.builder()
            .id(1L)
            .featureId(1L)
            .groupId(GROUP_ID)
            .enabled(true)
            .build();

        when(groupOverrideRepository.findByFeatureIdAndGroupId(1L, GROUP_ID)).thenReturn(Optional.of(override));

        service.deleteGroupOverride(1L, GROUP_ID);

        verify(groupOverrideRepository, times(1)).delete(override);
    }
}
