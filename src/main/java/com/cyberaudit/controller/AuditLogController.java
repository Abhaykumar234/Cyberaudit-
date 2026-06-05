package com.cyberaudit.controller;

import com.cyberaudit.model.AuditLog;
import com.cyberaudit.model.enums.Severity;
import com.cyberaudit.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<Page<AuditLog>> getAllLogs(Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getAllLogs(pageable));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<AuditLog>> getLogsByUser(
            @PathVariable String userId,
            Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getLogsByUser(userId, pageable));
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<Page<AuditLog>> getLogsBySeverity(
            @PathVariable Severity severity,
            Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getLogsBySeverity(severity, pageable));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<AuditLog>> getLogsByCategory(
            @PathVariable String category,
            Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getLogsByCategory(category, pageable));
    }
}
