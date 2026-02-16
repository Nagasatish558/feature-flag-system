package com.featureflag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private String message;
    private T data;
    private long timestamp;

    public ApiResponse(String message) {
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}
