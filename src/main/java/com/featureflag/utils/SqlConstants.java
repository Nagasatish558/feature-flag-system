package com.featureflag.utils;

/**
 * SQL and database-related constants
 */
public class SqlConstants {
    
    // Table names
    public static final String TABLE_FEATURE_FLAGS = "feature_flags";
    public static final String TABLE_FEATURE_USER_OVERRIDES = "feature_user_overrides";
    public static final String TABLE_FEATURE_GROUP_OVERRIDES = "feature_group_overrides";
    public static final String TABLE_USER_GROUPS = "user_groups";
    
    // Column names - feature_flags
    public static final String COL_FEATURE_KEY = "feature_key";
    public static final String COL_FEATURE_ID = "feature_id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_GROUP_ID = "group_id";
    public static final String COL_ENABLED = "enabled";
    
    // Parameter names for NamedJdbc
    public static final String PARAM_FEATURE_ID = "featureId";
    public static final String PARAM_USER_ID = "userId";
    public static final String PARAM_GROUP_IDS = "groupIds";
    
    private SqlConstants() {
        // Prevent instantiation
    }
}

