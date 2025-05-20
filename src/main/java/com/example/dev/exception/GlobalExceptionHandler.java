package com.example.dev.exception;

import com.example.dev.dto.response.ApiResponse;
import com.example.dev.enums.ErrorCode;
import jakarta.validation.constraints.Size;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(ErrorCode.UNCATEGORIZED.getCode());
        response.setMessage("An exception occurred: " + ErrorCode.UNCATEGORIZED.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(ErrorCode.DATA_INTEGRITY.getCode());
        response.setMessage("Data integrity violation: " + ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        Object target = ex.getBindingResult().getTarget();

        for (var fieldError : ex.getBindingResult().getFieldErrors()) {
            String messageKey = fieldError.getDefaultMessage();
            if (messageKey == null) continue;

            String fieldName = fieldError.getField();
            Map<String, String> placeholders = extractPlaceholders(messageKey);
            if (placeholders.isEmpty() && target != null) {
                placeholders = extractFromAnnotations(target, fieldName);
            }

            String enumKey = messageKey.split(":", 2)[0];
            String message = resolveMessage(enumKey, placeholders);

            errors.put(fieldName, message);
        }

        return buildValidationErrorResponse(errors);
    }
    private Map<String, String> extractPlaceholders(String message) {
        Map<String, String> placeholders = new HashMap<>();
        String[] parts = message.split(":", 2);
        if (parts.length < 2) return placeholders;

        String[] keyValues = parts[1].replaceAll("[{}]", "").split(",");
        for (String pair : keyValues) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                placeholders.put(kv[0].trim(), kv[1].trim());
            }
        }
        return placeholders;
    }

    private Map<String, String> extractFromAnnotations(Object target, String fieldName) {
        Map<String, String> placeholders = new HashMap<>();
        Field field = ReflectionUtils.findField(target.getClass(), fieldName);
        if (field == null) return placeholders;

        for (Annotation annotation : field.getAnnotations()) {
            if (annotation instanceof Size size) {
                placeholders.put("min", String.valueOf(size.min()));
                placeholders.put("max", String.valueOf(size.max()));
            }
            // Add more annotations here if needed
        }
        return placeholders;
    }

    private String resolveMessage(String enumKey, Map<String, String> placeholders) {
        try {
            ErrorCode errorCode = ErrorCode.valueOf(enumKey);
            String message = errorCode.getMessage();
            for (var entry : placeholders.entrySet()) {
                message = message.replace("{" + entry.getKey() + "}", entry.getValue());
            }
            return message;
        } catch (IllegalArgumentException e) {
            return ErrorCode.INVALID_KEY_ENUM.getMessage();
        }
    }

    private ResponseEntity<ApiResponse<Map<String, String>>> buildValidationErrorResponse(Map<String, String> errors) {
        ApiResponse<Map<String, String>> response = new ApiResponse<>();
        response.setCode(ErrorCode.UNCATEGORIZED.getCode());
        response.setMessage("Validation failed");
        response.setResult(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }




    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(errorCode.getCode());
        response.setMessage("An appException exception occurred: " + errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(response);
    }

    // 403
    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(RuntimeException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(errorCode.getCode());
        response.setMessage("Access denied: " + errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(response);
    }
}
