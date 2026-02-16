package com.featureflag.service.impl;

import com.featureflag.dto.UserGroupDTO;
import com.featureflag.entity.UserGroup;
import com.featureflag.entity.UserGroupId;
import com.featureflag.repository.api.IUserGroupRepository;
import com.featureflag.service.api.IUserGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserGroupService implements IUserGroupService {

    private final IUserGroupRepository userGroupRepository;

    @Override
    @CacheEvict(value = "evaluations", allEntries = true)
    public UserGroupDTO createUserGroup(String userId, String groupId) {
        log.info("Creating user group mapping - userId: {}, groupId: {}", userId, groupId);
        
        UserGroup userGroup = UserGroup.builder()
            .userId(userId)
            .groupId(groupId)
            .build();
        
        UserGroup savedUserGroup = userGroupRepository.save(userGroup);
        log.info("User group mapping created successfully");
        
        return mapToDTO(savedUserGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGroupDTO> getUserGroups(String userId) {
        log.info("Fetching groups for userId: {}", userId);
        List<UserGroup> userGroups = userGroupRepository.findByUserId(userId);
        return userGroups.stream()
            .map(this::mapToDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGroupDTO> getGroupUsers(String groupId) {
        log.info("Fetching users for groupId: {}", groupId);
        List<UserGroup> userGroups = userGroupRepository.findByGroupId(groupId);
        return userGroups.stream()
            .map(this::mapToDTO)
            .toList();
    }

    @Override
    @CacheEvict(value = "evaluations", allEntries = true)
    public void deleteUserGroup(String userId, String groupId) {
        log.info("Deleting user group mapping - userId: {}, groupId: {}", userId, groupId);
        UserGroupId id = new UserGroupId(userId, groupId);
        userGroupRepository.deleteById(id);
        log.info("User group mapping deleted successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGroupDTO> getAllUserGroups() {
        log.info("Fetching all user group mappings");
        List<UserGroup> userGroups = userGroupRepository.findAll();
        return userGroups.stream()
            .map(this::mapToDTO)
            .toList();
    }

    private UserGroupDTO mapToDTO(UserGroup userGroup) {
        return UserGroupDTO.builder()
            .userId(userGroup.getUserId())
            .groupId(userGroup.getGroupId())
            .build();
    }
}
