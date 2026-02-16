package com.featureflag.controller;

import com.featureflag.dto.CreateUserGroupRequest;
import com.featureflag.dto.UserGroupDTO;
import com.featureflag.service.api.IUserGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user-groups")
@Slf4j
@RequiredArgsConstructor
public class UserGroupController {

    private final IUserGroupService userGroupService;

    @PostMapping
    public ResponseEntity<UserGroupDTO> createUserGroup(@Valid @RequestBody CreateUserGroupRequest request) {
        log.info("POST /api/v1/user-groups - Creating user group: userId={}, groupId={}", 
                 request.getUserId(), request.getGroupId());
        UserGroupDTO userGroup = userGroupService.createUserGroup(
            request.getUserId(),
            request.getGroupId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(userGroup);
    }

    @GetMapping
    public ResponseEntity<List<UserGroupDTO>> getAllUserGroups() {
        log.info("GET /api/v1/user-groups - Fetching all user groups");
        List<UserGroupDTO> userGroups = userGroupService.getAllUserGroups();
        return ResponseEntity.ok(userGroups);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserGroupDTO>> getUserGroups(@PathVariable String userId) {
        log.info("GET /api/v1/user-groups/user/{} - Fetching groups for user", userId);
        List<UserGroupDTO> userGroups = userGroupService.getUserGroups(userId);
        return ResponseEntity.ok(userGroups);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<UserGroupDTO>> getGroupUsers(@PathVariable String groupId) {
        log.info("GET /api/v1/user-groups/group/{} - Fetching users in group", groupId);
        List<UserGroupDTO> users = userGroupService.getGroupUsers(groupId);
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{userId}/{groupId}")
    public ResponseEntity<Void> deleteUserGroup(
            @PathVariable String userId,
            @PathVariable String groupId) {
        log.info("DELETE /api/v1/user-groups/{}/{} - Removing user from group", userId, groupId);
        userGroupService.deleteUserGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }
}
