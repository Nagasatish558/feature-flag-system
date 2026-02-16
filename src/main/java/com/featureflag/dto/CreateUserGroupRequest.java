package com.featureflag.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserGroupRequest {
    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Group ID is required")
    private String groupId;
}
