package com.featureflag.exception;

public class FeatureFlagNotFoundException extends FeatureFlagException {
    public FeatureFlagNotFoundException(String message) {
        super(message);
    }
}
