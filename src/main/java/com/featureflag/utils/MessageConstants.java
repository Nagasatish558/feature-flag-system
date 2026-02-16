package com.featureflag.utils;

/**
 * Application-wide message constants
 */
public class MessageConstants {
    
    // Feature Flag Messages
    public static final String FEATURE_NOT_FOUND = "Feature flag not found with ID: ";
    public static final String FEATURE_BY_KEY_NOT_FOUND = "Feature flag not found with key: ";
    public static final String DUPLICATE_OVERRIDE = "User override already exists for feature ID: ";
    public static final String OVERRIDE_NOT_FOUND = "Override not found for feature ID: ";
    
    // Service Operations
    public static final String CREATING_USER_OVERRIDE = "Creating user override for feature ID: {}, user: {}, enabled: {}";
    public static final String USER_OVERRIDE_CREATED = "User override created successfully";
    public static final String DELETING_USER_OVERRIDE = "Deleting user override for feature ID: {}, user: {}";
    public static final String USER_OVERRIDE_DELETED = "User override deleted successfully";
    
    public static final String CREATING_GROUP_OVERRIDE = "Creating group override for feature ID: {}, group: {}, enabled: {}";
    public static final String GROUP_OVERRIDE_CREATED = "Group override created successfully";
    public static final String DELETING_GROUP_OVERRIDE = "Deleting group override for feature ID: {}, group: {}";
    public static final String GROUP_OVERRIDE_DELETED = "Group override deleted successfully";
    
    public static final String CREATING_FLAG = "Creating feature flag with key: {}";
    public static final String FLAG_CREATED = "Feature flag created successfully with ID: {}";
    public static final String GETTING_FLAG = "Getting feature flag with ID: {}";
    public static final String GETTING_FLAG_BY_KEY = "Getting feature flag by key: {}";
    public static final String UPDATING_FLAG = "Updating feature flag with ID: {}";
    public static final String FLAG_UPDATED = "Feature flag updated successfully with ID: {}";
    public static final String DELETING_FLAG = "Deleting feature flag with ID: {}";
    public static final String FLAG_DELETED = "Feature flag deleted successfully with ID: {}";
    
    public static final String EVALUATING_FEATURE = "Evaluating feature: {} for user: {}";
    public static final String USER_OVERRIDE_FOUND = "User override found for feature {}: {}";
    public static final String GROUP_OVERRIDE_FOUND = "Group override found for feature {}, group {}: {}";
    public static final String USING_DEFAULT_STATE = "Using default state for feature {}: {}";
    
    private MessageConstants() {
        // Prevent instantiation
    }
}
