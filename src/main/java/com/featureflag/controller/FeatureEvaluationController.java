package com.featureflag.controller;

import com.featureflag.dto.FeatureEvaluationDTO;
import com.featureflag.service.api.IFeatureEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/evaluate")
@Slf4j
@RequiredArgsConstructor
public class FeatureEvaluationController {

    private final IFeatureEvaluationService evaluationService;

    @GetMapping("/{featureKey}/{userId}")
    public ResponseEntity<FeatureEvaluationDTO> evaluateFeature(
            @PathVariable String featureKey,
            @PathVariable String userId) {
        log.info("GET /api/v1/evaluate/{}/{} - Evaluating feature for user", featureKey, userId);
        FeatureEvaluationDTO evaluation = evaluationService.evaluateFeature(featureKey, userId);
        return ResponseEntity.ok(evaluation);
    }

    @GetMapping("/default/{featureKey}")
    public ResponseEntity<Boolean> getDefaultState(@PathVariable String featureKey) {
        log.info("GET /api/v1/evaluate/default/{} - Getting default state", featureKey);
        Boolean defaultState = evaluationService.getDefaultState(featureKey);
        return ResponseEntity.ok(defaultState);
    }
}
