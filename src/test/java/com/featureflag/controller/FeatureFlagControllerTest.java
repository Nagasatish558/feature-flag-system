package com.featureflag.controller;

import com.featureflag.dto.CreateFeatureFlagRequest;
import com.featureflag.dto.FeatureFlagDTO;
import com.featureflag.exception.FeatureFlagNotFoundException;
import com.featureflag.service.api.IFeatureFlagService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeatureFlagController.class)
@DisplayName("FeatureFlagController Tests")
class FeatureFlagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IFeatureFlagService featureFlagService;

    private FeatureFlagDTO testFlagDTO;
    private CreateFeatureFlagRequest createRequest;

    @BeforeEach
    void setUp() {
        testFlagDTO = FeatureFlagDTO.builder()
                .id(1L)
                .featureKey("test-feature")
                .description("Test feature flag")
                .defaultEnabled(true)
                .build();

        createRequest = CreateFeatureFlagRequest.builder()
                .featureKey("new-feature")
                .description("New feature flag")
                .defaultEnabled(false)
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/flags - Create feature flag successfully")
    void testCreateFlag_Success() throws Exception {
        when(featureFlagService.createFlag(anyString(), anyString(), anyBoolean()))
                .thenReturn(testFlagDTO);

        mockMvc.perform(post("/api/v1/flags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.featureKey").value("test-feature"));

        verify(featureFlagService, times(1)).createFlag(anyString(), anyString(), anyBoolean());
    }

    @Test
    @DisplayName("POST /api/v1/flags - Validation fails with null featureKey")
    void testCreateFlag_ValidationFails() throws Exception {
        CreateFeatureFlagRequest invalidRequest = CreateFeatureFlagRequest.builder()
                .featureKey(null)
                .description("No key provided")
                .defaultEnabled(true)
                .build();

        mockMvc.perform(post("/api/v1/flags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(featureFlagService, times(0)).createFlag(anyString(), anyString(), anyBoolean());
    }

    @Test
    @DisplayName("GET /api/v1/flags/{id} - Get feature flag by ID successfully")
    void testGetFlag_Success() throws Exception {
        when(featureFlagService.getFlag(1L)).thenReturn(testFlagDTO);

        mockMvc.perform(get("/api/v1/flags/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.featureKey").value("test-feature"));

        verify(featureFlagService, times(1)).getFlag(1L);
    }

    @Test
    @DisplayName("GET /api/v1/flags/{id} - Feature not found")
    void testGetFlag_NotFound() throws Exception {
        when(featureFlagService.getFlag(999L))
                .thenThrow(new FeatureFlagNotFoundException("Feature flag not found"));

        mockMvc.perform(get("/api/v1/flags/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(featureFlagService, times(1)).getFlag(999L);
    }

    @Test
    @DisplayName("GET /api/v1/flags/key/{featureKey} - Get feature by key successfully")
    void testGetFlagByKey_Success() throws Exception {
        when(featureFlagService.getFlagByKey("test-feature")).thenReturn(testFlagDTO);

        mockMvc.perform(get("/api/v1/flags/key/test-feature")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.featureKey").value("test-feature"));

        verify(featureFlagService, times(1)).getFlagByKey("test-feature");
    }

    @Test
    @DisplayName("GET /api/v1/flags/key/{featureKey} - Feature key not found")
    void testGetFlagByKey_NotFound() throws Exception {
        when(featureFlagService.getFlagByKey("non-existent"))
                .thenThrow(new FeatureFlagNotFoundException("Feature flag not found"));

        mockMvc.perform(get("/api/v1/flags/key/non-existent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(featureFlagService, times(1)).getFlagByKey("non-existent");
    }

    @Test
    @DisplayName("PUT /api/v1/flags/{id} - Update feature flag successfully")
    void testUpdateFlag_Success() throws Exception {
        FeatureFlagDTO updatedFlagDTO = FeatureFlagDTO.builder()
                .id(1L)
                .featureKey("test-feature")
                .description("Updated description")
                .defaultEnabled(false)
                .build();

        CreateFeatureFlagRequest updateRequest = CreateFeatureFlagRequest.builder()
                .featureKey("test-feature")
                .description("Updated description")
                .defaultEnabled(false)
                .build();

        when(featureFlagService.updateFlag(anyLong(), anyString(), anyBoolean()))
                .thenReturn(updatedFlagDTO);

        mockMvc.perform(put("/api/v1/flags/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.defaultEnabled").value(false));

        verify(featureFlagService, times(1)).updateFlag(anyLong(), anyString(), anyBoolean());
    }

    @Test
    @DisplayName("PUT /api/v1/flags/{id} - Feature not found on update")
    void testUpdateFlag_NotFound() throws Exception {
        CreateFeatureFlagRequest updateRequest = CreateFeatureFlagRequest.builder()
                .featureKey("test-feature")
                .description("Updated")
                .defaultEnabled(false)
                .build();

        when(featureFlagService.updateFlag(anyLong(), anyString(), anyBoolean()))
                .thenThrow(new FeatureFlagNotFoundException("Feature flag not found"));

        mockMvc.perform(put("/api/v1/flags/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        verify(featureFlagService, times(1)).updateFlag(anyLong(), anyString(), anyBoolean());
    }

    @Test
    @DisplayName("DELETE /api/v1/flags/{id} - Delete feature flag successfully")
    void testDeleteFlag_Success() throws Exception {
        doNothing().when(featureFlagService).deleteFlag(1L);

        mockMvc.perform(delete("/api/v1/flags/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(featureFlagService, times(1)).deleteFlag(1L);
    }

    @Test
    @DisplayName("DELETE /api/v1/flags/{id} - Feature not found on delete")
    void testDeleteFlag_NotFound() throws Exception {
        doThrow(new FeatureFlagNotFoundException("Feature flag not found"))
                .when(featureFlagService).deleteFlag(999L);

        mockMvc.perform(delete("/api/v1/flags/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(featureFlagService, times(1)).deleteFlag(999L);
    }
}
