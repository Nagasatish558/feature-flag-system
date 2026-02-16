package com.featureflag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_groups", indexes = {
    @Index(name = "idx_user_groups", columnList = "user_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(UserGroupId.class)
public class UserGroup {

    @Id
    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    @Id
    @Column(name = "group_id", nullable = false, length = 100)
    private String groupId;
}
