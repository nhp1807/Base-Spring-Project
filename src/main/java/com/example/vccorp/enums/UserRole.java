package com.example.vccorp.enums;

public enum UserRole {
    ADMIN("Admin"),
    EDITORIAL("Editorial"),
    BRAND("Brand");

    private String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
