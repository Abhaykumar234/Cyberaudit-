package com.cyberaudit.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AuditResponse {
    
    private Long id;
    private String targetId;
    private String auditType;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer findingsCount;
    private Integer criticalCount;
    private Integer highCount;
    private String summary;
    private List<FindingDto> findings;
    
    // Constructors
    public AuditResponse() {}
    
    public AuditResponse(Long id, String targetId, String auditType, String status, LocalDateTime startTime,
                        LocalDateTime endTime, Integer findingsCount, Integer criticalCount, Integer highCount,
                        String summary, List<FindingDto> findings) {
        this.id = id;
        this.targetId = targetId;
        this.auditType = auditType;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.findingsCount = findingsCount;
        this.criticalCount = criticalCount;
        this.highCount = highCount;
        this.summary = summary;
        this.findings = findings;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    
    public String getAuditType() { return auditType; }
    public void setAuditType(String auditType) { this.auditType = auditType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public Integer getFindingsCount() { return findingsCount; }
    public void setFindingsCount(Integer findingsCount) { this.findingsCount = findingsCount; }
    
    public Integer getCriticalCount() { return criticalCount; }
    public void setCriticalCount(Integer criticalCount) { this.criticalCount = criticalCount; }
    
    public Integer getHighCount() { return highCount; }
    public void setHighCount(Integer highCount) { this.highCount = highCount; }
    
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    
    public List<FindingDto> getFindings() { return findings; }
    public void setFindings(List<FindingDto> findings) { this.findings = findings; }
}
