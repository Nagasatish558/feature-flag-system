package com.featureflag.repository.api;

import com.featureflag.entity.UserGroup;
import com.featureflag.entity.UserGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Interface for UserGroup persistence operations
 * Follows Dependency Inversion Principle (DIP)
 */
@Repository
public interface IUserGroupRepository extends JpaRepository<UserGroup, UserGroupId> {
    List<UserGroup> findByUserId(String userId);
    List<UserGroup> findByGroupId(String groupId);
    void deleteByUserIdAndGroupId(String userId, String groupId);
}
