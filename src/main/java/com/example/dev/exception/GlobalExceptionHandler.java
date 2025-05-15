package com.example.dev.exception;

import com.example.dev.dto.response.ApiResponse;
import com.example.dev.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (var fieldError : ex.getBindingResult().getFieldErrors()) {
            String enumKey = fieldError.getDefaultMessage();

            ErrorCode errorCode;
            try {
                errorCode = ErrorCode.valueOf(enumKey);
            } catch (IllegalArgumentException e) {
                errorCode = ErrorCode.INVALID_KEY_ENUM;
            }

            errors.put(fieldError.getField(), errorCode.getMessage());
        }

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

    //401


}
