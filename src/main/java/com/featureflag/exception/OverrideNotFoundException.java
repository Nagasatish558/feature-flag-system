package com.featureflag.exception;

public class OverrideNotFoundException extends FeatureFlagException {
    public OverrideNotFoundException(String message) {
        super(message);
    }
}
