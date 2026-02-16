package com.featureflag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a user override
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserOverrideRequest {
    
    @NotNull(message = "Feature ID is required")
    private Long featureId;
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;
}
