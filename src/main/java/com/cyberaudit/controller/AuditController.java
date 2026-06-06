package com.cyberaudit.controller;

import com.cyberaudit.dto.AuditRequest;
import com.cyberaudit.dto.AuditResponse;
import com.cyberaudit.dto.FindingDto;
import com.cyberaudit.model.AuditTarget;
import com.cyberaudit.service.AuditTargetService;
import com.cyberaudit.service.ClaudeAiService;
import com.cyberaudit.service.VulnerabilityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final ClaudeAiService claudeAiService;
    private final VulnerabilityService vulnerabilityService;
    private final AuditTargetService targetService;

    public AuditController(
            ClaudeAiService claudeAiService,
            VulnerabilityService vulnerabilityService,
            AuditTargetService targetService) {
        this.claudeAiService = claudeAiService;
        this.vulnerabilityService = vulnerabilityService;
        this.targetService = targetService;
    }

    @PostMapping("/generate")
    public ResponseEntity<AuditResponse> generateAudit(
            @Valid @RequestBody AuditRequest request,
            HttpServletRequest httpRequest) {
        enrichRequestWithTarget(request);
        AuditResponse response = claudeAiService.generateAudit(request, resolveClientIp(httpRequest));
        if (response.getFindings() != null) {
            for (FindingDto finding : response.getFindings()) {
                try {
                    vulnerabilityService.upsertFromFinding(finding);
                } catch (Exception ignored) {
                    // Skip findings with invalid severity values
                }
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "running",
                "aiConfigured", String.valueOf(claudeAiService.isConfigured())));
    }

    private void enrichRequestWithTarget(AuditRequest request) {
        try {
            Long targetId = Long.parseLong(request.getTargetId());
            targetService.getTargetById(targetId).ifPresent(target -> applyTargetContext(request, target));
        } catch (NumberFormatException ignored) {
            // targetId may be a free-form identifier
        }
    }

    private void applyTargetContext(AuditRequest request, AuditTarget target) {
        request.setScope(target.getEndpoint());
        if (request.getAuditType() == null || request.getAuditType().isBlank()) {
            request.setAuditType("FULL");
        }
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
