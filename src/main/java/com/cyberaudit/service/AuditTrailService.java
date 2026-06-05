package com.cyberaudit.service;

import com.cyberaudit.model.AuditLog;
import com.cyberaudit.model.enums.Severity;
import com.cyberaudit.model.enums.Status;
import com.cyberaudit.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditTrailService {

    private final AuditLogRepository auditLogRepository;

    public AuditTrailService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void log(String userId, String action, String description, Severity severity,
                    String category, String sourceIp, String affectedResource) {
        AuditLog entry = new AuditLog();
        entry.setUserId(userId);
        entry.setAction(action);
        entry.setDescription(description);
        entry.setSeverity(severity);
        entry.setStatus(Status.OPEN);
        entry.setCategory(category);
        entry.setSourceIp(sourceIp != null ? sourceIp : "unknown");
        entry.setAffectedResource(affectedResource);
        auditLogRepository.save(entry);
    }
}
