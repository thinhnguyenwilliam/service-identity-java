package com.example.dev.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_EXISTED(1002, "User existed"),
    UNCATEGORIZED(1003,"Uncategorized error"),
    USER_INVALID(1004,"Username must be at least 3 characters long hi hi"),
    INVALID_KEY_ENUM(1006,"Invalid key enum"),
    USER_NOT_EXISTED(1007,"User not existed"),
    UNAUTHENTICATED(1008,"Unauthenticated bro"),
    PASSWORD_INVALID(1005,"Password must be at least 6 characters long ha ha");

    private final int code;
    private final String message;
}

