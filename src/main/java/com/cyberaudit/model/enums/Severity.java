package com.cyberaudit.model.enums;

public enum Severity {
    CRITICAL("Critical", "#F43F5E"),
    HIGH("High", "#F59E0B"),
    MEDIUM("Medium", "#FBBF24"),
    LOW("Low", "#10B981"),
    INFO("Info", "#3B82F6");

    private final String label;
    private final String color;

    Severity(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public String getColor() {
        return color;
    }
}
