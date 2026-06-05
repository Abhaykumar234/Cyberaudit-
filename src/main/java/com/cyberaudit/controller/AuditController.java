package com.cyberaudit.controller;

import com.cyberaudit.dto.AuditRequest;
import com.cyberaudit.dto.AuditResponse;
import com.cyberaudit.dto.FindingDto;
import com.cyberaudit.service.ClaudeAiService;
import com.cyberaudit.service.VulnerabilityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final ClaudeAiService claudeAiService;
    private final VulnerabilityService vulnerabilityService;

    public AuditController(ClaudeAiService claudeAiService, VulnerabilityService vulnerabilityService) {
        this.claudeAiService = claudeAiService;
        this.vulnerabilityService = vulnerabilityService;
    }

    @PostMapping("/generate")
    public ResponseEntity<AuditResponse> generateAudit(
            @Valid @RequestBody AuditRequest request,
            HttpServletRequest httpRequest) {
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
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Audit service is running");
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
