package com.featureflag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a group override
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateGroupOverrideRequest {
    
    @NotNull(message = "Feature ID is required")
    private Long featureId;
    
    @NotBlank(message = "Group ID is required")
    private String groupId;
    
    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;
}
