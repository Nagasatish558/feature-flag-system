package com.featureflag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureFlagDTO {
    private Long id;
    private String featureKey;
    private String description;
    private Boolean defaultEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
