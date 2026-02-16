package com.featureflag.controller;

import com.featureflag.dto.CreateFeatureFlagRequest;
import com.featureflag.dto.FeatureFlagDTO;
import com.featureflag.service.api.IFeatureFlagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/flags")
@Slf4j
@RequiredArgsConstructor
public class FeatureFlagController {

    private final IFeatureFlagService featureFlagService;

    @PostMapping
    public ResponseEntity<FeatureFlagDTO> createFlag(@Valid @RequestBody CreateFeatureFlagRequest request) {
        log.info("POST /api/v1/flags - Creating feature flag: {}", request.getFeatureKey());
        FeatureFlagDTO flag = featureFlagService.createFlag(
            request.getFeatureKey(),
            request.getDescription(),
            request.getDefaultEnabled()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(flag);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeatureFlagDTO> getFlag(@PathVariable Long id) {
        log.info("GET /api/v1/flags/{} - Fetching feature flag", id);
        FeatureFlagDTO flag = featureFlagService.getFlag(id);
        return ResponseEntity.ok(flag);
    }

    @GetMapping("/key/{featureKey}")
    public ResponseEntity<FeatureFlagDTO> getFlagByKey(@PathVariable String featureKey) {
        log.info("GET /api/v1/flags/key/{} - Fetching feature flag by key", featureKey);
        FeatureFlagDTO flag = featureFlagService.getFlagByKey(featureKey);
        return ResponseEntity.ok(flag);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeatureFlagDTO> updateFlag(
            @PathVariable Long id,
            @Valid @RequestBody CreateFeatureFlagRequest request) {
        log.info("PUT /api/v1/flags/{} - Updating feature flag", id);
        FeatureFlagDTO flag = featureFlagService.updateFlag(
            id,
            request.getDescription(),
            request.getDefaultEnabled()
        );
        return ResponseEntity.ok(flag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlag(@PathVariable Long id) {
        log.info("DELETE /api/v1/flags/{} - Deleting feature flag", id);
        featureFlagService.deleteFlag(id);
        return ResponseEntity.noContent().build();
    }
}
