package com.github.donnyk22.models.enums;

public enum UserRole {
    ADMIN(Constants.ADMIN), 
    STUDENT(Constants.STUDENT), 
    TEACHER(Constants.TEACHER);

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String securityVal() {
        return "ROLE_" + this.role;
    }

    public static class Constants {
        public static final String ADMIN = "ADMIN";
        public static final String STUDENT = "STUDENT";
        public static final String TEACHER = "TEACHER";
    }
}