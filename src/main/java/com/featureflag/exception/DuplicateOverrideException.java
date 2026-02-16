package com.featureflag.exception;

public class DuplicateOverrideException extends FeatureFlagException {
    public DuplicateOverrideException(String message) {
        super(message);
    }
}
