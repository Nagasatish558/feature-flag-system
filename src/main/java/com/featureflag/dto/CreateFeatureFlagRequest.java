package com.featureflag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFeatureFlagRequest {
    
    @NotBlank(message = "Feature key is required")
    @Size(min = 1, max = 100, message = "Feature key must be between 1 and 100 characters")
    private String featureKey;
    
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
    
    @NotNull(message = "Default enabled state is required")
    private Boolean defaultEnabled;
}
