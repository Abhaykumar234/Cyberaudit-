package com.cyberaudit.dto;

import jakarta.validation.constraints.NotBlank;

public class LabRequest {
    
    @NotBlank(message = "Lab mission is required")
    private String mission;
    
    @NotBlank(message = "Code snippet is required")
    private String codeSnippet;
    
    private String language;
    
    private String vulnerabilityType;
    
    // Constructors
    public LabRequest() {}
    
    public LabRequest(String mission, String codeSnippet, String language, String vulnerabilityType) {
        this.mission = mission;
        this.codeSnippet = codeSnippet;
        this.language = language;
        this.vulnerabilityType = vulnerabilityType;
    }
    
    // Getters and Setters
    public String getMission() { return mission; }
    public void setMission(String mission) { this.mission = mission; }
    
    public String getCodeSnippet() { return codeSnippet; }
    public void setCodeSnippet(String codeSnippet) { this.codeSnippet = codeSnippet; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public String getVulnerabilityType() { return vulnerabilityType; }
    public void setVulnerabilityType(String vulnerabilityType) { this.vulnerabilityType = vulnerabilityType; }
}
