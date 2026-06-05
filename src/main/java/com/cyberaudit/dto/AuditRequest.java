package com.cyberaudit.dto;

import jakarta.validation.constraints.NotBlank;

public class AuditRequest {
    
    @NotBlank(message = "Target ID is required")
    private String targetId;
    
    @NotBlank(message = "Audit type is required")
    private String auditType;
    
    private String scope;
    
    private Boolean includeRemediationAdvice;
    
    // Constructors
    public AuditRequest() {}
    
    public AuditRequest(String targetId, String auditType, String scope, Boolean includeRemediationAdvice) {
        this.targetId = targetId;
        this.auditType = auditType;
        this.scope = scope;
        this.includeRemediationAdvice = includeRemediationAdvice;
    }
    
    // Getters and Setters
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    
    public String getAuditType() { return auditType; }
    public void setAuditType(String auditType) { this.auditType = auditType; }
    
    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }
    
    public Boolean getIncludeRemediationAdvice() { return includeRemediationAdvice; }
    public void setIncludeRemediationAdvice(Boolean includeRemediationAdvice) { 
        this.includeRemediationAdvice = includeRemediationAdvice; 
    }
}
