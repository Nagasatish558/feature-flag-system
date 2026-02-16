package com.featureflag.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(FeatureFlagNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFeatureFlagNotFound(
            FeatureFlagNotFoundException ex,
            WebRequest request) {
        log.warn("Feature flag not found: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .message(ex.getMessage())
            .error("FEATURE_FLAG_NOT_FOUND")
            .timestamp(LocalDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateOverrideException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateOverride(
            DuplicateOverrideException ex,
            WebRequest request) {
        log.warn("Duplicate override: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.CONFLICT.value())
            .message(ex.getMessage())
            .error("DUPLICATE_OVERRIDE")
            .timestamp(LocalDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OverrideNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOverrideNotFound(
            OverrideNotFoundException ex,
            WebRequest request) {
        log.warn("Override not found: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .message(ex.getMessage())
            .error("OVERRIDE_NOT_FOUND")
            .timestamp(LocalDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        log.warn("Validation error: {}", ex.getMessage());
        
        String message = ex.getBindingResult().getAllErrors().stream()
            .map(error -> error.getDefaultMessage())
            .findFirst()
            .orElse("Validation failed");
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .message(message)
            .error("VALIDATION_ERROR")
            .timestamp(LocalDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request) {
        log.warn("Data integrity violation: {}", ex.getMessage());
        
        String message = "Duplicate entry or constraint violation";
        if (ex.getMessage() != null && ex.getMessage().contains("feature_key")) {
            message = "A feature flag with this key already exists";
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.CONFLICT.value())
            .message(message)
            .error("DUPLICATE_ENTRY")
            .timestamp(LocalDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex,
            WebRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("An unexpected error occurred")
            .error("INTERNAL_SERVER_ERROR")
            .timestamp(LocalDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
