package com.cyberaudit.model.enums;

public enum Role {
    SUPER_ADMIN("Super Administrator"),
    SENIOR_DEVELOPER("Senior Developer"),
    COMPLIANCE_AUDITOR("Compliance Auditor"),
    SECURITY_ENGINEER("Security Engineer"),
    VIEWER("Viewer");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
