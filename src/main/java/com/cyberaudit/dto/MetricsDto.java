package com.cyberaudit.dto;

public class MetricsDto {
    
    private Double postureScore;
    private Integer activeFindings;
    private Integer criticalCount;
    private Integer highCount;
    private Integer mediumCount;
    private Integer totalFindings;
    private Integer remediationRate;
    private Integer assetsInScope;
    private String globalThreatLevel;
    private Integer activeEndpoints;
    private String engineStatus;
    
    // Constructors
    public MetricsDto() {}
    
    public MetricsDto(Double postureScore, Integer activeFindings, Integer criticalCount, Integer highCount,
                     Integer remediationRate, Integer assetsInScope, String globalThreatLevel,
                     Integer activeEndpoints, String engineStatus) {
        this.postureScore = postureScore;
        this.activeFindings = activeFindings;
        this.criticalCount = criticalCount;
        this.highCount = highCount;
        this.remediationRate = remediationRate;
        this.assetsInScope = assetsInScope;
        this.globalThreatLevel = globalThreatLevel;
        this.activeEndpoints = activeEndpoints;
        this.engineStatus = engineStatus;
    }
    
    // Getters and Setters
    public Double getPostureScore() { return postureScore; }
    public void setPostureScore(Double postureScore) { this.postureScore = postureScore; }
    
    public Integer getActiveFindings() { return activeFindings; }
    public void setActiveFindings(Integer activeFindings) { this.activeFindings = activeFindings; }
    
    public Integer getCriticalCount() { return criticalCount; }
    public void setCriticalCount(Integer criticalCount) { this.criticalCount = criticalCount; }
    
    public Integer getHighCount() { return highCount; }
    public void setHighCount(Integer highCount) { this.highCount = highCount; }

    public Integer getMediumCount() { return mediumCount; }
    public void setMediumCount(Integer mediumCount) { this.mediumCount = mediumCount; }

    public Integer getTotalFindings() { return totalFindings; }
    public void setTotalFindings(Integer totalFindings) { this.totalFindings = totalFindings; }

    public Integer getRemediationRate() { return remediationRate; }
    public void setRemediationRate(Integer remediationRate) { this.remediationRate = remediationRate; }
    
    public Integer getAssetsInScope() { return assetsInScope; }
    public void setAssetsInScope(Integer assetsInScope) { this.assetsInScope = assetsInScope; }
    
    public String getGlobalThreatLevel() { return globalThreatLevel; }
    public void setGlobalThreatLevel(String globalThreatLevel) { this.globalThreatLevel = globalThreatLevel; }
    
    public Integer getActiveEndpoints() { return activeEndpoints; }
    public void setActiveEndpoints(Integer activeEndpoints) { this.activeEndpoints = activeEndpoints; }
    
    public String getEngineStatus() { return engineStatus; }
    public void setEngineStatus(String engineStatus) { this.engineStatus = engineStatus; }
}
