package com.featureflag.service.impl;

import com.featureflag.dto.UserGroupDTO;
import com.featureflag.entity.UserGroup;
import com.featureflag.repository.api.IUserGroupRepository;
import com.featureflag.service.api.IUserGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserGroupService Tests")
class UserGroupServiceTest {

    private static final String USER_ID = "user123";
    private static final String ADMIN_GROUP = "admin";
    private static final String USERS_GROUP = "users";
    private static final String ORPHAN_USER = "orphan-user";
    private static final String EMPTY_GROUP = "empty-group";
    private static final String TEST_USER = "testUser";
    private static final String TEST_GROUP = "testGroup";
    private static final String USER_1 = "user1";
    private static final String USER_2 = "user2";

    @Mock
    private IUserGroupRepository userGroupRepository;

    private IUserGroupService userGroupService;

    @BeforeEach
    void setUp() {
        userGroupService = new UserGroupService(userGroupRepository);
    }

    @Test
    @DisplayName("Should create user-group association successfully")
    void testCreateUserGroup() {
        // Arrange
        String userId = USER_ID;
        String groupId = ADMIN_GROUP;
        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(userId);
        userGroup.setGroupId(groupId);

        when(userGroupRepository.save(any(UserGroup.class))).thenReturn(userGroup);

        // Act
        UserGroupDTO result = userGroupService.createUserGroup(userId, groupId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(groupId, result.getGroupId());
        verify(userGroupRepository, times(1)).save(any(UserGroup.class));
    }

    @Test
    @DisplayName("Should retrieve user groups successfully")
    void testGetUserGroups() {
        // Arrange
        String userId = USER_ID;
        UserGroup group1 = new UserGroup();
        group1.setUserId(userId);
        group1.setGroupId(ADMIN_GROUP);

        UserGroup group2 = new UserGroup();
        group2.setUserId(userId);
        group2.setGroupId(USERS_GROUP);

        when(userGroupRepository.findByUserId(userId))
                .thenReturn(Arrays.asList(group1, group2));

        // Act
        List<UserGroupDTO> result = userGroupService.getUserGroups(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(ADMIN_GROUP, result.get(0).getGroupId());
        assertEquals(USERS_GROUP, result.get(1).getGroupId());
        verify(userGroupRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Should return empty list when user has no groups")
    void testGetUserGroupsEmpty() {
        // Arrange
        String userId = ORPHAN_USER;
        when(userGroupRepository.findByUserId(userId)).thenReturn(Arrays.asList());

        // Act
        List<UserGroupDTO> result = userGroupService.getUserGroups(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userGroupRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Should retrieve group users successfully")
    void testGetGroupUsers() {
        // Arrange
        String groupId = ADMIN_GROUP;
        UserGroup user1 = new UserGroup();
        user1.setUserId(USER_1);
        user1.setGroupId(groupId);

        UserGroup user2 = new UserGroup();
        user2.setUserId(USER_2);
        user2.setGroupId(groupId);

        when(userGroupRepository.findByGroupId(groupId))
                .thenReturn(Arrays.asList(user1, user2));

        // Act
        List<UserGroupDTO> result = userGroupService.getGroupUsers(groupId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(USER_1, result.get(0).getUserId());
        assertEquals(USER_2, result.get(1).getUserId());
        verify(userGroupRepository, times(1)).findByGroupId(groupId);
    }

    @Test
    @DisplayName("Should return empty list when group has no users")
    void testGetGroupUsersEmpty() {
        // Arrange
        String groupId = EMPTY_GROUP;
        when(userGroupRepository.findByGroupId(groupId)).thenReturn(Arrays.asList());

        // Act
        List<UserGroupDTO> result = userGroupService.getGroupUsers(groupId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userGroupRepository, times(1)).findByGroupId(groupId);
    }

    @Test
    @DisplayName("Should delete user-group association successfully")
    void testDeleteUserGroup() {
        // Arrange
        String userId = USER_ID;
        String groupId = ADMIN_GROUP;

        // Act
        userGroupService.deleteUserGroup(userId, groupId);

        // Assert
        verify(userGroupRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("Should delete multiple times without error")
    void testDeleteUserGroupMultiple() {
        // Arrange
        String userId = USER_ID;
        String groupId = ADMIN_GROUP;

        // Act & Assert
        assertDoesNotThrow(() -> {
            userGroupService.deleteUserGroup(userId, groupId);
            userGroupService.deleteUserGroup(userId, groupId);
        });

        verify(userGroupRepository, times(2)).deleteById(any());
    }

    @Test
    @DisplayName("Should retrieve all user-group associations")
    void testGetAllUserGroups() {
        // Arrange
        UserGroup assoc1 = new UserGroup();
        assoc1.setUserId(USER_1);
        assoc1.setGroupId(ADMIN_GROUP);

        UserGroup assoc2 = new UserGroup();
        assoc2.setUserId(USER_2);
        assoc2.setGroupId(USERS_GROUP);

        when(userGroupRepository.findAll()).thenReturn(Arrays.asList(assoc1, assoc2));

        // Act
        List<UserGroupDTO> result = userGroupService.getAllUserGroups();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userGroupRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no associations exist")
    void testGetAllUserGroupsEmpty() {
        // Arrange
        when(userGroupRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<UserGroupDTO> result = userGroupService.getAllUserGroups();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userGroupRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should map UserGroup entity to DTO correctly")
    void testUserGroupMapping() {
        // Arrange
        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(TEST_USER);
        userGroup.setGroupId(TEST_GROUP);

        when(userGroupRepository.save(any(UserGroup.class))).thenReturn(userGroup);

        // Act
        UserGroupDTO dto = userGroupService.createUserGroup(TEST_USER, TEST_GROUP);

        // Assert
        assertNotNull(dto);
        assertEquals(TEST_USER, dto.getUserId());
        assertEquals(TEST_GROUP, dto.getGroupId());
    }

    @Test
    @DisplayName("Should handle null userId gracefully")
    void testCreateUserGroupWithNullUserId() {
        // Act & Assert
        assertThrows(Exception.class, () -> 
            userGroupService.createUserGroup(null, ADMIN_GROUP)
        );
    }

    @Test
    @DisplayName("Should handle null groupId gracefully")
    void testCreateUserGroupWithNullGroupId() {
        // Act & Assert
        assertThrows(Exception.class, () -> 
            userGroupService.createUserGroup(USER_ID, null)
        );
    }

    @Test
    @DisplayName("Should handle special characters in userId")
    void testCreateUserGroupWithSpecialCharacters() {
        // Arrange
        String userId = "user@example.com";
        String groupId = "admin-group";
        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(userId);
        userGroup.setGroupId(groupId);

        when(userGroupRepository.save(any(UserGroup.class))).thenReturn(userGroup);

        // Act
        UserGroupDTO result = userGroupService.createUserGroup(userId, groupId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(groupId, result.getGroupId());
    }
}
