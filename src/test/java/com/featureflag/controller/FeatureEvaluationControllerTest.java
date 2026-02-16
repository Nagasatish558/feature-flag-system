package com.featureflag.controller;

import com.featureflag.dto.FeatureEvaluationDTO;
import com.featureflag.exception.FeatureFlagNotFoundException;
import com.featureflag.service.api.IFeatureEvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeatureEvaluationController.class)
@DisplayName("FeatureEvaluationController Tests")
class FeatureEvaluationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFeatureEvaluationService featureEvaluationService;

    private FeatureEvaluationDTO evaluationDTO;

    @BeforeEach
    void setUp() {
        evaluationDTO = FeatureEvaluationDTO.builder()
                .enabled(true)
                .reason("USER_OVERRIDE")
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/evaluate/{featureKey}/{userId} - Evaluate feature for user successfully")
    void testEvaluateFeature_Success() throws Exception {
        when(featureEvaluationService.evaluateFeature("dark-mode", "alice"))
                .thenReturn(evaluationDTO);

        mockMvc.perform(get("/api/v1/evaluate/dark-mode/alice")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.reason").value("USER_OVERRIDE"));

        verify(featureEvaluationService, times(1)).evaluateFeature("dark-mode", "alice");
    }

    @Test
    @DisplayName("GET /api/v1/evaluate/{featureKey}/{userId} - Feature not found")
    void testEvaluateFeature_FeatureNotFound() throws Exception {
        when(featureEvaluationService.evaluateFeature("non-existent", "alice"))
                .thenThrow(new FeatureFlagNotFoundException("Feature flag not found"));

        mockMvc.perform(get("/api/v1/evaluate/non-existent/alice")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(featureEvaluationService, times(1)).evaluateFeature("non-existent", "alice");
    }

    @Test
    @DisplayName("GET /api/v1/evaluate/{featureKey}/{userId} - Group override evaluation")
    void testEvaluateFeature_GroupOverride() throws Exception {
        FeatureEvaluationDTO groupOverrideDTO = FeatureEvaluationDTO.builder()
                .enabled(true)
                .reason("GROUP_OVERRIDE")
                .build();

        when(featureEvaluationService.evaluateFeature("premium-feature", "charlie"))
                .thenReturn(groupOverrideDTO);

        mockMvc.perform(get("/api/v1/evaluate/premium-feature/charlie")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.reason").value("GROUP_OVERRIDE"));

        verify(featureEvaluationService, times(1)).evaluateFeature("premium-feature", "charlie");
    }

    @Test
    @DisplayName("GET /api/v1/evaluate/{featureKey}/{userId} - Default state evaluation")
    void testEvaluateFeature_DefaultState() throws Exception {
        FeatureEvaluationDTO defaultDTO = FeatureEvaluationDTO.builder()
                .enabled(false)
                .reason("DEFAULT")
                .build();

        when(featureEvaluationService.evaluateFeature("beta-feature", "dave"))
                .thenReturn(defaultDTO);

        mockMvc.perform(get("/api/v1/evaluate/beta-feature/dave")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(false))
                .andExpect(jsonPath("$.reason").value("DEFAULT"));

        verify(featureEvaluationService, times(1)).evaluateFeature("beta-feature", "dave");
    }

    @Test
    @DisplayName("GET /api/v1/evaluate/default/{featureKey} - Get default state successfully")
    void testGetDefaultState_Success() throws Exception {
        when(featureEvaluationService.getDefaultState("test-feature"))
                .thenReturn(true);

        mockMvc.perform(get("/api/v1/evaluate/default/test-feature")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(featureEvaluationService, times(1)).getDefaultState("test-feature");
    }

    @Test
    @DisplayName("GET /api/v1/evaluate/default/{featureKey} - Feature not found")
    void testGetDefaultState_FeatureNotFound() throws Exception {
        when(featureEvaluationService.getDefaultState("non-existent"))
                .thenThrow(new FeatureFlagNotFoundException("Feature flag not found"));

        mockMvc.perform(get("/api/v1/evaluate/default/non-existent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(featureEvaluationService, times(1)).getDefaultState("non-existent");
    }
}
