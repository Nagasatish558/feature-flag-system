package com.featureflag.controller;

import com.featureflag.dto.CreateGroupOverrideRequest;
import com.featureflag.dto.CreateUserOverrideRequest;
import com.featureflag.service.api.IOverrideService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/overrides")
@Slf4j
@RequiredArgsConstructor
public class OverrideController {

    private final IOverrideService overrideService;
    private final HttpServletRequest request;

    // User Overrides
    @PostMapping("/users")
    public ResponseEntity<Void> createUserOverride(
            @Valid @RequestBody CreateUserOverrideRequest request) {
        log.info("POST {} - Creating user override", this.request.getRequestURI());
        overrideService.createUserOverride(request.getFeatureId(), request.getUserId(), request.getEnabled());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/users/{featureId}/{userId}")
    public ResponseEntity<Void> deleteUserOverride(
            @PathVariable Long featureId,
            @PathVariable String userId) {
        log.info("DELETE {} - Deleting user override", request.getRequestURI());
        overrideService.deleteUserOverride(featureId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/groups")
    public ResponseEntity<Void> createGroupOverride(
            @Valid @RequestBody CreateGroupOverrideRequest request) {
        log.info("POST {} - Creating group override", this.request.getRequestURI());
        overrideService.createGroupOverride(request.getFeatureId(), request.getGroupId(), request.getEnabled());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/groups/{featureId}/{groupId}")
    public ResponseEntity<Void> deleteGroupOverride(
            @PathVariable Long featureId,
            @PathVariable String groupId) {
        log.info("DELETE {} - Deleting group override", request.getRequestURI());
        overrideService.deleteGroupOverride(featureId, groupId);
        return ResponseEntity.noContent().build();
    }
}
