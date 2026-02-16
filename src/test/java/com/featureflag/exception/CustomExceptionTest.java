package com.featureflag.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Custom Exception Tests")
class CustomExceptionTest {

    @Test
    @DisplayName("FeatureFlagException - Create with message")
    void testFeatureFlagException() {
        String message = "Feature flag error";
        FeatureFlagException exception = new FeatureFlagException(message);

        assertEquals(message, exception.getMessage());
        assertNotNull(exception);
    }

    @Test
    @DisplayName("FeatureFlagException - Create with message and cause")
    void testFeatureFlagExceptionWithCause() {
        String message = "Feature flag error";
        Throwable cause = new RuntimeException("Root cause");
        FeatureFlagException exception = new FeatureFlagException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("FeatureFlagNotFoundException - Create and verify inheritance")
    void testFeatureFlagNotFoundException() {
        String message = "Feature flag not found";
        FeatureFlagNotFoundException exception = new FeatureFlagNotFoundException(message);

        assertEquals(message, exception.getMessage());
        assertNotNull(exception);
        assertTrue(exception instanceof FeatureFlagException);
    }

    @Test
    @DisplayName("DuplicateOverrideException - Create and verify inheritance")
    void testDuplicateOverrideException() {
        String message = "Duplicate override already exists";
        DuplicateOverrideException exception = new DuplicateOverrideException(message);

        assertEquals(message, exception.getMessage());
        assertNotNull(exception);
        assertTrue(exception instanceof FeatureFlagException);
    }

    @Test
    @DisplayName("OverrideNotFoundException - Create and verify inheritance")
    void testOverrideNotFoundException() {
        String message = "Override not found";
        OverrideNotFoundException exception = new OverrideNotFoundException(message);

        assertEquals(message, exception.getMessage());
        assertNotNull(exception);
        assertTrue(exception instanceof FeatureFlagException);
    }

    @Test
    @DisplayName("Multiple exception instances have independent messages")
    void testMultipleExceptionInstances() {
        FeatureFlagNotFoundException notFound = new FeatureFlagNotFoundException("Not found");
        DuplicateOverrideException duplicate = new DuplicateOverrideException("Duplicate");

        assertEquals("Not found", notFound.getMessage());
        assertEquals("Duplicate", duplicate.getMessage());
    }
}
