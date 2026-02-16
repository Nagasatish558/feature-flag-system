package com.featureflag.service.api;

import com.featureflag.dto.UserGroupDTO;
import java.util.List;

public interface IUserGroupService {
    UserGroupDTO createUserGroup(String userId, String groupId);
    List<UserGroupDTO> getUserGroups(String userId);
    List<UserGroupDTO> getGroupUsers(String groupId);
    void deleteUserGroup(String userId, String groupId);
    List<UserGroupDTO> getAllUserGroups();
}
