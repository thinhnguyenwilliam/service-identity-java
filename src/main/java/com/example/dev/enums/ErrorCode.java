package com.example.dev.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_EXISTED(1002, "User existed", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED(1003,"Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_INVALID(1004,"Username must be at least 3 characters long hi hi", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY_ENUM(1006,"Invalid key enum", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_EXISTED(1007,"User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1008,"Unauthenticated bro",HttpStatus.UNAUTHORIZED),
    PASSWORD_INVALID(1005,"Password must be at least {min} characters long ha ha", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(1008,"You do not have permission", HttpStatus.FORBIDDEN),
    DATA_INTEGRITY(1009,"Many to Many relationship", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_DOB(1010,"User must be at least {min} years old", HttpStatus.BAD_REQUEST)
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}

