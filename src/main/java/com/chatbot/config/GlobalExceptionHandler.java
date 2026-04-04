package com.chatbot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global error handler — returns clean, structured error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        log.error("❌ Validation failed: {}", fieldErrors);

        return ResponseEntity.badRequest().body(buildError(
            "VALIDATION_FAILED",
            "Request body validation failed. Please check the required fields.",
            fieldErrors
        ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMalformedJson(HttpMessageNotReadableException ex) {
        log.error("❌ Malformed JSON received: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(buildError(
            "INVALID_JSON",
            "Request body is not valid JSON. Please send a properly formatted JSON payload.",
            null
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(Exception ex) {
        log.error("❌ Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildError(
            "INTERNAL_ERROR",
            "Something went wrong on our end. Please try again later.",
            null
        ));
    }

    private Map<String, Object> buildError(String code, String message, Object details) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", code);
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now().format(FORMATTER));
        if (details != null) body.put("details", details);
        return body;
    }
}
