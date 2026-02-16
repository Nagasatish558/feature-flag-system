package com.featureflag.repository.impl;

import com.featureflag.repository.api.IFeatureFlagQueryRepository;
import com.featureflag.utils.SqlConstants;
import com.featureflag.utils.SqlQueries;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FeatureFlagQueryRepository implements IFeatureFlagQueryRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public FeatureFlagQueryRepository(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    /**
     * Fast query to get all feature keys
     */
    public List<String> getAllFeatureKeys() {
        return namedJdbcTemplate.getJdbcTemplate()
            .queryForList(SqlQueries.GET_ALL_FEATURE_KEYS, String.class);
    }

    /**
     * Get user's groups efficiently (cached for 60s)
     */
    @Cacheable(value = "evaluations", key = "'user_groups:' + #userId", unless = "#result == null || #result.isEmpty()")
    public List<String> getUserGroups(String userId) {
        Map<String, Object> params = new HashMap<>();
        params.put(SqlConstants.PARAM_USER_ID, userId);
        return namedJdbcTemplate.queryForList(SqlQueries.GET_USER_GROUPS, params, String.class);
    }

    /**
     * Check if user has override for a feature
     */
    public boolean hasUserOverride(Long featureId, String userId) {
        Map<String, Object> params = new HashMap<>();
        params.put(SqlConstants.PARAM_FEATURE_ID, featureId);
        params.put(SqlConstants.PARAM_USER_ID, userId);
        Integer count = namedJdbcTemplate.queryForObject(
            SqlQueries.COUNT_USER_OVERRIDES, 
            params, 
            Integer.class
        );
        return count != null && count > 0;
    }

    /**
     * Get user override state (cached for hot path)
     */
    @Cacheable(value = "evaluations", key = "'user_override:' + #featureId + ':' + #userId")
    public Boolean getUserOverrideState(Long featureId, String userId) {
        Map<String, Object> params = new HashMap<>();
        params.put(SqlConstants.PARAM_FEATURE_ID, featureId);
        params.put(SqlConstants.PARAM_USER_ID, userId);
        try {
            Object result = namedJdbcTemplate.queryForObject(
                SqlQueries.GET_USER_OVERRIDE_STATE,
                params,
                Object.class
            );
            return convertToBoolean(result);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Get group override states for a feature (cached for hot path)
     */
    @Cacheable(value = "evaluations", key = "'group_overrides:' + #featureId + ':' + #groupIds.hashCode()")
    public Map<String, Boolean> getGroupOverrideStates(Long featureId, List<String> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return new HashMap<>();
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put(SqlConstants.PARAM_FEATURE_ID, featureId);
        params.put(SqlConstants.PARAM_GROUP_IDS, groupIds);
        
        List<Map<String, Object>> results = namedJdbcTemplate.queryForList(
            SqlQueries.GET_GROUP_OVERRIDE_STATES,
            params
        );
        
        Map<String, Boolean> overrides = new HashMap<>();
        for (Map<String, Object> row : results) {
            String groupId = (String) row.get(SqlConstants.COL_GROUP_ID);
            Boolean enabled = convertToBoolean(row.get(SqlConstants.COL_ENABLED));
            overrides.put(groupId, enabled);
        }
        return overrides;
    }
    
    /**
     * Convert database value to Boolean (handles both Integer and Boolean types from MySQL)
     */
    private Boolean convertToBoolean(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Integer) {
            return ((Integer) value) != 0;
        }
        return Boolean.parseBoolean(value.toString());
    }
}


