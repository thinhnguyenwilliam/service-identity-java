package com.example.dev.constant;

public class PredefinedRole {
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    private PredefinedRole() {}
}

// privilege(permission)

// user -> role: many to many
// role -> permissions: many to many