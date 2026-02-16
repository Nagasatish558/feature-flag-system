package com.featureflag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureEvaluationDTO {
    private String featureKey;
    private String userId;
    private Boolean enabled;
    private String reason; // USER_OVERRIDE, GROUP_OVERRIDE, or DEFAULT
}
