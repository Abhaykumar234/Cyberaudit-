package com.cyberaudit.dto;

public class FindingDto {
    
    private String id;
    private String title;
    private String severity;
    private String category;
    private String description;
    private String threatVector;
    private String remediationAdvice;
    private Integer occurrences;
    private Integer affectedEndpoints;
    
    // Constructors
    public FindingDto() {}
    
    public FindingDto(String id, String title, String severity, String category, String description,
                     String threatVector, String remediationAdvice, Integer occurrences, Integer affectedEndpoints) {
        this.id = id;
        this.title = title;
        this.severity = severity;
        this.category = category;
        this.description = description;
        this.threatVector = threatVector;
        this.remediationAdvice = remediationAdvice;
        this.occurrences = occurrences;
        this.affectedEndpoints = affectedEndpoints;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getThreatVector() { return threatVector; }
    public void setThreatVector(String threatVector) { this.threatVector = threatVector; }
    
    public String getRemediationAdvice() { return remediationAdvice; }
    public void setRemediationAdvice(String remediationAdvice) { this.remediationAdvice = remediationAdvice; }
    
    public Integer getOccurrences() { return occurrences; }
    public void setOccurrences(Integer occurrences) { this.occurrences = occurrences; }
    
    public Integer getAffectedEndpoints() { return affectedEndpoints; }
    public void setAffectedEndpoints(Integer affectedEndpoints) { this.affectedEndpoints = affectedEndpoints; }
}
