package com.featureflag.controller;

import com.featureflag.dto.CreateGroupOverrideRequest;
import com.featureflag.dto.CreateUserOverrideRequest;
import com.featureflag.exception.DuplicateOverrideException;
import com.featureflag.exception.FeatureFlagNotFoundException;
import com.featureflag.exception.OverrideNotFoundException;
import com.featureflag.service.api.IOverrideService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OverrideController.class)
@DisplayName("OverrideController Tests")
class OverrideControllerTest {

    // API Endpoints
    private static final String API_V1_OVERRIDES_USERS = "/api/v1/overrides/users";
    private static final String API_V1_OVERRIDES_GROUPS = "/api/v1/overrides/groups";
    private static final String API_V1_OVERRIDES_USERS_WITH_ID = "/api/v1/overrides/users/{featureId}/{userId}";
    private static final String API_V1_OVERRIDES_GROUPS_WITH_ID = "/api/v1/overrides/groups/{featureId}/{groupId}";

    // Test Data
    private static final long FEATURE_ID = 1L;
    private static final long INVALID_FEATURE_ID = 999L;
    private static final String USER_ID = "alice";
    private static final String GROUP_ID = "admin";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IOverrideService overrideService;

    private CreateUserOverrideRequest userOverrideRequest;
    
    private CreateGroupOverrideRequest groupOverrideRequest;

    @BeforeEach
    void setUp() {
        userOverrideRequest = CreateUserOverrideRequest.builder()
                .featureId(FEATURE_ID)
                .userId(USER_ID)
                .enabled(true)
                .build();

        groupOverrideRequest = CreateGroupOverrideRequest.builder()
                .featureId(FEATURE_ID)
                .groupId(GROUP_ID)
                .enabled(true)
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/overrides/users - Create user override successfully")
    void testCreateUserOverride_Success() throws Exception {
        doNothing().when(overrideService).createUserOverride(FEATURE_ID, USER_ID, true);

        mockMvc.perform(post(API_V1_OVERRIDES_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userOverrideRequest)))
                .andExpect(status().isCreated());

        verify(overrideService, times(1)).createUserOverride(FEATURE_ID, USER_ID, true);
    }

    @Test
    @DisplayName("POST /api/v1/overrides/users - Feature not found")
    void testCreateUserOverride_FeatureNotFound() throws Exception {
        doThrow(new FeatureFlagNotFoundException("Feature flag not found"))
                .when(overrideService).createUserOverride(INVALID_FEATURE_ID, USER_ID, true);

        userOverrideRequest.setFeatureId(INVALID_FEATURE_ID);

        mockMvc.perform(post(API_V1_OVERRIDES_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userOverrideRequest)))
                .andExpect(status().isNotFound());

        verify(overrideService, times(1)).createUserOverride(INVALID_FEATURE_ID, USER_ID, true);
    }

    @Test
    @DisplayName("POST /api/v1/overrides/users - Duplicate override")
    void testCreateUserOverride_DuplicateOverride() throws Exception {
        doThrow(new DuplicateOverrideException("User override already exists for this feature"))
                .when(overrideService).createUserOverride(FEATURE_ID, USER_ID, true);

        mockMvc.perform(post(API_V1_OVERRIDES_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userOverrideRequest)))
                .andExpect(status().isConflict());

        verify(overrideService, times(1)).createUserOverride(FEATURE_ID, USER_ID, true);
    }

    @Test
    @DisplayName("POST /api/v1/overrides/users - Validation fails with null userId")
    void testCreateUserOverride_ValidationFails_NullUserId() throws Exception {
        CreateUserOverrideRequest invalidRequest = CreateUserOverrideRequest.builder()
                .featureId(FEATURE_ID)
                .userId(null)
                .enabled(true)
                .build();

        mockMvc.perform(post(API_V1_OVERRIDES_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(overrideService, times(0)).createUserOverride(anyLong(), anyString(), anyBoolean());
    }

    @Test
    @DisplayName("POST /api/v1/overrides/users - Validation fails with null featureId")
    void testCreateUserOverride_ValidationFails_NullFeatureId() throws Exception {
        CreateUserOverrideRequest invalidRequest = CreateUserOverrideRequest.builder()
                .featureId(null)
                .userId(USER_ID)
                .enabled(true)
                .build();

        mockMvc.perform(post(API_V1_OVERRIDES_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(overrideService, times(0)).createUserOverride(anyLong(), anyString(), anyBoolean());
    }

    @Test
    @DisplayName("DELETE /api/v1/overrides/users/{featureId}/{userId} - Delete user override successfully")
    void testDeleteUserOverride_Success() throws Exception {
        doNothing().when(overrideService).deleteUserOverride(FEATURE_ID, USER_ID);

        mockMvc.perform(delete(API_V1_OVERRIDES_USERS_WITH_ID, FEATURE_ID, USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(overrideService, times(1)).deleteUserOverride(FEATURE_ID, USER_ID);
    }

    @Test
    @DisplayName("DELETE /api/v1/overrides/users/{featureId}/{userId} - Override not found")
    void testDeleteUserOverride_NotFound() throws Exception {
        doThrow(new OverrideNotFoundException("User override not found"))
                .when(overrideService).deleteUserOverride(INVALID_FEATURE_ID, USER_ID);

        mockMvc.perform(delete(API_V1_OVERRIDES_USERS_WITH_ID, INVALID_FEATURE_ID, USER_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(overrideService, times(1)).deleteUserOverride(INVALID_FEATURE_ID, USER_ID);
    }

    @Test
    @DisplayName("POST /api/v1/overrides/groups - Create group override successfully")
    void testCreateGroupOverride_Success() throws Exception {
        doNothing().when(overrideService).createGroupOverride(FEATURE_ID, GROUP_ID, true);

        mockMvc.perform(post(API_V1_OVERRIDES_GROUPS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupOverrideRequest)))
                .andExpect(status().isCreated());

        verify(overrideService, times(1)).createGroupOverride(FEATURE_ID, GROUP_ID, true);
    }

    @Test
    @DisplayName("POST /api/v1/overrides/groups - Feature not found")
    void testCreateGroupOverride_FeatureNotFound() throws Exception {
        doThrow(new FeatureFlagNotFoundException("Feature flag not found"))
                .when(overrideService).createGroupOverride(INVALID_FEATURE_ID, GROUP_ID, true);

        groupOverrideRequest.setFeatureId(INVALID_FEATURE_ID);

        mockMvc.perform(post(API_V1_OVERRIDES_GROUPS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupOverrideRequest)))
                .andExpect(status().isNotFound());

        verify(overrideService, times(1)).createGroupOverride(INVALID_FEATURE_ID, GROUP_ID, true);
    }

    @Test
    @DisplayName("POST /api/v1/overrides/groups - Duplicate override")
    void testCreateGroupOverride_DuplicateOverride() throws Exception {
        doThrow(new DuplicateOverrideException("Group override already exists for this feature"))
                .when(overrideService).createGroupOverride(FEATURE_ID, GROUP_ID, true);

        mockMvc.perform(post(API_V1_OVERRIDES_GROUPS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupOverrideRequest)))
                .andExpect(status().isConflict());

        verify(overrideService, times(1)).createGroupOverride(FEATURE_ID, GROUP_ID, true);
    }

    @Test
    @DisplayName("POST /api/v1/overrides/groups - Validation fails with null groupId")
    void testCreateGroupOverride_ValidationFails_NullGroupId() throws Exception {
        CreateGroupOverrideRequest invalidRequest = CreateGroupOverrideRequest.builder()
                .featureId(FEATURE_ID)
                .groupId(null)
                .enabled(true)
                .build();

        mockMvc.perform(post(API_V1_OVERRIDES_GROUPS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(overrideService, times(0)).createGroupOverride(anyLong(), anyString(), anyBoolean());
    }

    @Test
    @DisplayName("DELETE /api/v1/overrides/groups/{featureId}/{groupId} - Delete group override successfully")
    void testDeleteGroupOverride_Success() throws Exception {
        doNothing().when(overrideService).deleteGroupOverride(FEATURE_ID, GROUP_ID);

        mockMvc.perform(delete(API_V1_OVERRIDES_GROUPS_WITH_ID, FEATURE_ID, GROUP_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(overrideService, times(1)).deleteGroupOverride(FEATURE_ID, GROUP_ID);
    }

    @Test
    @DisplayName("DELETE /api/v1/overrides/groups/{featureId}/{groupId} - Override not found")
    void testDeleteGroupOverride_NotFound() throws Exception {
        doThrow(new OverrideNotFoundException("Group override not found"))
                .when(overrideService).deleteGroupOverride(INVALID_FEATURE_ID, GROUP_ID);

        mockMvc.perform(delete(API_V1_OVERRIDES_GROUPS_WITH_ID, INVALID_FEATURE_ID, GROUP_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(overrideService, times(1)).deleteGroupOverride(INVALID_FEATURE_ID, GROUP_ID);
    }
}
