package com.featureflag.repository.api;

import com.featureflag.entity.UserGroup;
import com.featureflag.entity.UserGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for UserGroup entity
 * Follows Dependency Inversion Principle (DIP)
 */
@Repository
public interface IFeatureUserGroupRepository extends JpaRepository<UserGroup, UserGroupId> {
    
    /**
     * Find all groups for a given user
     */
    List<UserGroup> findByUserId(String userId);
}
