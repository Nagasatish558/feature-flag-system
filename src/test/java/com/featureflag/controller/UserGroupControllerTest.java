package com.featureflag.controller;

import com.featureflag.dto.CreateUserGroupRequest;
import com.featureflag.dto.UserGroupDTO;
import com.featureflag.service.api.IUserGroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserGroupController Tests")
class UserGroupControllerTest {

    @Mock
    private IUserGroupService userGroupService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserGroupController(userGroupService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should create user-group association successfully")
    void testCreateUserGroup() throws Exception {
        // Arrange
        CreateUserGroupRequest request = new CreateUserGroupRequest("user123", "admin");
        UserGroupDTO dto = UserGroupDTO.builder()
                .userId("user123")
                .groupId("admin")
                .build();

        when(userGroupService.createUserGroup(anyString(), anyString())).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/user-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.groupId").value("admin"));

        verify(userGroupService, times(1)).createUserGroup("user123", "admin");
    }

    @Test
    @DisplayName("Should return 400 when userId is missing")
    void testCreateUserGroupMissingUserId() throws Exception {
        // Arrange
        String json = "{\"groupId\": \"admin\"}";

        // Act & Assert
        mockMvc.perform(post("/api/v1/user-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when groupId is missing")
    void testCreateUserGroupMissingGroupId() throws Exception {
        // Arrange
        String json = "{\"userId\": \"user123\"}";

        // Act & Assert
        mockMvc.perform(post("/api/v1/user-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should retrieve all user-group associations")
    void testGetAllUserGroups() throws Exception {
        // Arrange
        UserGroupDTO dto1 = UserGroupDTO.builder()
                .userId("user1")
                .groupId("admin")
                .build();

        UserGroupDTO dto2 = UserGroupDTO.builder()
                .userId("user2")
                .groupId("users")
                .build();

        when(userGroupService.getAllUserGroups()).thenReturn(Arrays.asList(dto1, dto2));

        // Act & Assert
        mockMvc.perform(get("/api/v1/user-groups")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[1].userId").value("user2"));

        verify(userGroupService, times(1)).getAllUserGroups();
    }

    @Test
    @DisplayName("Should return empty list when no associations exist")
    void testGetAllUserGroupsEmpty() throws Exception {
        // Arrange
        when(userGroupService.getAllUserGroups()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/user-groups")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(userGroupService, times(1)).getAllUserGroups();
    }

    @Test
    @DisplayName("Should retrieve groups for a specific user")
    void testGetUserGroups() throws Exception {
        // Arrange
        String userId = "user123";
        UserGroupDTO dto1 = UserGroupDTO.builder()
                .userId(userId)
                .groupId("admin")
                .build();

        UserGroupDTO dto2 = UserGroupDTO.builder()
                .userId(userId)
                .groupId("users")
                .build();

        when(userGroupService.getUserGroups(userId)).thenReturn(Arrays.asList(dto1, dto2));

        // Act & Assert
        mockMvc.perform(get("/api/v1/user-groups/user/user123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].groupId").value("admin"))
                .andExpect(jsonPath("$[1].groupId").value("users"));

        verify(userGroupService, times(1)).getUserGroups("user123");
    }

    @Test
    @DisplayName("Should return empty list when user has no groups")
    void testGetUserGroupsEmpty() throws Exception {
        // Arrange
        String userId = "orphan-user";
        when(userGroupService.getUserGroups(userId)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/user-groups/user/orphan-user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(userGroupService, times(1)).getUserGroups("orphan-user");
    }

    @Test
    @DisplayName("Should retrieve users in a specific group")
    void testGetGroupUsers() throws Exception {
        // Arrange
        String groupId = "admin";
        UserGroupDTO dto1 = UserGroupDTO.builder()
                .userId("user1")
                .groupId(groupId)
                .build();

        UserGroupDTO dto2 = UserGroupDTO.builder()
                .userId("user2")
                .groupId(groupId)
                .build();

        when(userGroupService.getGroupUsers(groupId)).thenReturn(Arrays.asList(dto1, dto2));

        // Act & Assert
        mockMvc.perform(get("/api/v1/user-groups/group/admin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[1].userId").value("user2"));

        verify(userGroupService, times(1)).getGroupUsers("admin");
    }

    @Test
    @DisplayName("Should return empty list when group has no users")
    void testGetGroupUsersEmpty() throws Exception {
        // Arrange
        String groupId = "empty-group";
        when(userGroupService.getGroupUsers(groupId)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/user-groups/group/empty-group")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(userGroupService, times(1)).getGroupUsers("empty-group");
    }

    @Test
    @DisplayName("Should delete user-group association successfully")
    void testDeleteUserGroup() throws Exception {
        // Arrange
        String userId = "user123";
        String groupId = "admin";

        doNothing().when(userGroupService).deleteUserGroup(userId, groupId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/user-groups/user123/admin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userGroupService, times(1)).deleteUserGroup("user123", "admin");
    }

    @Test
    @DisplayName("Should return 204 even if association doesn't exist")
    void testDeleteUserGroupNotFound() throws Exception {
        // Arrange
        String userId = "user-not-found";
        String groupId = "admin";

        doNothing().when(userGroupService).deleteUserGroup(userId, groupId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/user-groups/user-not-found/admin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userGroupService, times(1)).deleteUserGroup("user-not-found", "admin");
    }

    @Test
    @DisplayName("Should handle special characters in userId path")
    void testGetUserGroupsWithSpecialCharacters() throws Exception {
        // Arrange
        String userId = "user@example.com";
        UserGroupDTO dto = UserGroupDTO.builder()
                .userId(userId)
                .groupId("admin")
                .build();

        when(userGroupService.getUserGroups(userId)).thenReturn(Arrays.asList(dto));

        // Act & Assert
        mockMvc.perform(get("/api/v1/user-groups/user/user@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(userGroupService, times(1)).getUserGroups(userId);
    }

    @Test
    @DisplayName("Should handle multiple groups for user")
    void testGetUserGroupsMultiple() throws Exception {
        // Arrange
        String userId = "user123";
        UserGroupDTO[] dtos = new UserGroupDTO[5];
        String[] groupIds = {"admin", "users", "developers", "managers", "guests"};

        for (int i = 0; i < 5; i++) {
            dtos[i] = UserGroupDTO.builder()
                    .userId(userId)
                    .groupId(groupIds[i])
                    .build();
        }

        when(userGroupService.getUserGroups(userId)).thenReturn(Arrays.asList(dtos));

        // Act & Assert
        mockMvc.perform(get("/api/v1/user-groups/user/user123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));

        verify(userGroupService, times(1)).getUserGroups("user123");
    }

    @Test
    @DisplayName("Should delete with multiple calls")
    void testDeleteUserGroupMultiple() throws Exception {
        // Arrange
        doNothing().when(userGroupService).deleteUserGroup(anyString(), anyString());

        // Act & Assert - First delete
        mockMvc.perform(delete("/api/v1/user-groups/user123/admin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Act & Assert - Second delete
        mockMvc.perform(delete("/api/v1/user-groups/user123/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userGroupService, times(2)).deleteUserGroup(anyString(), anyString());
    }

    @Test
    @DisplayName("Should create multiple user-group associations")
    void testCreateMultipleUserGroups() throws Exception {
        // Arrange
        when(userGroupService.createUserGroup(anyString(), anyString()))
                .thenAnswer(inv -> UserGroupDTO.builder()
                        .userId((String) inv.getArgument(0))
                        .groupId((String) inv.getArgument(1))
                        .build());

        CreateUserGroupRequest request1 = new CreateUserGroupRequest("user1", "admin");
        CreateUserGroupRequest request2 = new CreateUserGroupRequest("user2", "users");

        // Act & Assert - First creation
        mockMvc.perform(post("/api/v1/user-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user1"));

        // Act & Assert - Second creation
        mockMvc.perform(post("/api/v1/user-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user2"));

        verify(userGroupService, times(2)).createUserGroup(anyString(), anyString());
    }
}
