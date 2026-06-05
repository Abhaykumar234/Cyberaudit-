package com.cyberaudit.dto;

public class LabResponse {
    
    private String missionId;
    private String status;
    private String vulnerabilityAnalysis;
    private String secureRepairLogic;
    private String tacticalHints;
    private String testSuite;
    private Boolean isVulnerable;
    
    // Constructors
    public LabResponse() {}
    
    public LabResponse(String missionId, String status, String vulnerabilityAnalysis, String secureRepairLogic,
                      String tacticalHints, String testSuite, Boolean isVulnerable) {
        this.missionId = missionId;
        this.status = status;
        this.vulnerabilityAnalysis = vulnerabilityAnalysis;
        this.secureRepairLogic = secureRepairLogic;
        this.tacticalHints = tacticalHints;
        this.testSuite = testSuite;
        this.isVulnerable = isVulnerable;
    }
    
    // Getters and Setters
    public String getMissionId() { return missionId; }
    public void setMissionId(String missionId) { this.missionId = missionId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getVulnerabilityAnalysis() { return vulnerabilityAnalysis; }
    public void setVulnerabilityAnalysis(String vulnerabilityAnalysis) { 
        this.vulnerabilityAnalysis = vulnerabilityAnalysis; 
    }
    
    public String getSecureRepairLogic() { return secureRepairLogic; }
    public void setSecureRepairLogic(String secureRepairLogic) { this.secureRepairLogic = secureRepairLogic; }
    
    public String getTacticalHints() { return tacticalHints; }
    public void setTacticalHints(String tacticalHints) { this.tacticalHints = tacticalHints; }
    
    public String getTestSuite() { return testSuite; }
    public void setTestSuite(String testSuite) { this.testSuite = testSuite; }
    
    public Boolean getIsVulnerable() { return isVulnerable; }
    public void setIsVulnerable(Boolean isVulnerable) { this.isVulnerable = isVulnerable; }
}
