package com.cyberaudit.service;

import com.cyberaudit.model.AuditLog;
import com.cyberaudit.model.enums.Severity;
import com.cyberaudit.repository.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AuditLogService {
    
    private final AuditLogRepository auditLogRepository;
    
    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }
    
    public AuditLog createLog(AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }
    
    @Transactional(readOnly = true)
    public Page<AuditLog> getLogsByUser(String userId, Pageable pageable) {
        return auditLogRepository.findByUserId(userId, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<AuditLog> getLogsBySeverity(Severity severity, Pageable pageable) {
        return auditLogRepository.findBySeverity(severity, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<AuditLog> getLogsByCategory(String category, Pageable pageable) {
        return auditLogRepository.findByCategory(category, pageable);
    }
    
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsBetweenDates(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findByTimestampBetween(start, end);
    }
    
    @Transactional(readOnly = true)
    public Long getCriticalLogCount() {
        return auditLogRepository.countBySeverity(Severity.CRITICAL);
    }
    
    @Transactional(readOnly = true)
    public Page<AuditLog> getAllLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }
}
