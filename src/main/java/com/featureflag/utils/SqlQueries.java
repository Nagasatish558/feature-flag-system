package com.featureflag.utils;

/**
 * Pre-built SQL query constants
 * Stores complete queries for better readability and maintainability
 */
public class SqlQueries {
    
    // Feature Flag Queries
    public static final String GET_ALL_FEATURE_KEYS = 
        "SELECT feature_key FROM feature_flags";
    
    // User Groups Queries
    public static final String GET_USER_GROUPS = 
        "SELECT group_id FROM user_groups WHERE user_id = :userId";
    
    // User Override Queries
    public static final String COUNT_USER_OVERRIDES = 
        "SELECT COUNT(*) FROM feature_user_overrides " +
        "WHERE feature_id = :featureId AND user_id = :userId";
    
    public static final String GET_USER_OVERRIDE_STATE = 
        "SELECT enabled FROM feature_user_overrides " +
        "WHERE feature_id = :featureId AND user_id = :userId";
    
    // Group Override Queries
    public static final String GET_GROUP_OVERRIDE_STATES = 
        "SELECT group_id, enabled FROM feature_group_overrides " +
        "WHERE feature_id = :featureId AND group_id IN (:groupIds)";
    
    private SqlQueries() {
        // Prevent instantiation
    }
}
