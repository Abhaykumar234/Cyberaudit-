package com.cyberaudit.service;

import com.cyberaudit.model.AuditTarget;
import com.cyberaudit.model.Vulnerability;
import com.cyberaudit.model.enums.Severity;
import com.cyberaudit.model.enums.Status;
import com.cyberaudit.repository.AuditTargetRepository;
import com.cyberaudit.repository.VulnerabilityRepository;
import com.cyberaudit.scanner.VulnerabilityScanner;
import com.cyberaudit.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class RealTimeScanService {

    private static final Logger logger = LoggerFactory.getLogger(RealTimeScanService.class);

    private final VulnerabilityScanner vulnerabilityScanner;
    private final AuditTargetRepository targetRepository;
    private final VulnerabilityRepository vulnerabilityRepository;
    private final AuditTrailService auditTrailService;

    public RealTimeScanService(
            VulnerabilityScanner vulnerabilityScanner,
            AuditTargetRepository targetRepository,
            VulnerabilityRepository vulnerabilityRepository,
            AuditTrailService auditTrailService) {
        this.vulnerabilityScanner = vulnerabilityScanner;
        this.targetRepository = targetRepository;
        this.vulnerabilityRepository = vulnerabilityRepository;
        this.auditTrailService = auditTrailService;
    }

    @Async
    public CompletableFuture<VulnerabilityScanner.ScanResult> scanTargetAsync(Long targetId, String sourceIp) {
        return CompletableFuture.completedFuture(scanTargetSync(targetId, sourceIp));
    }

    public VulnerabilityScanner.ScanResult scanTargetSync(Long targetId, String sourceIp) {
        logger.info("Starting scan for target ID: {}", targetId);

        AuditTarget target = targetRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("Target not found: " + targetId));

        VulnerabilityScanner.ScanResult scanResult = vulnerabilityScanner.scanTarget(target.getEndpoint());

        saveDiscoveredVulnerabilities(scanResult);
        updateTargetMetrics(target, scanResult);

        auditTrailService.log(
                currentUsername(),
                "TARGET_SCANNED",
                "Scanned " + target.getEndpoint() + " — " + scanResult.getFindings().size() + " findings",
                scanResult.getSeverityCount().getOrDefault("CRITICAL", 0) > 0 ? Severity.CRITICAL : Severity.MEDIUM,
                "SCAN",
                sourceIp,
                target.getEndpoint());

        return scanResult;
    }

    public VulnerabilityScanner.ScanResult scanUrlSync(String targetUrl, String sourceIp) {
        String normalized = normalizeUrl(targetUrl);
        VulnerabilityScanner.ScanResult scanResult = vulnerabilityScanner.scanTarget(normalized);

        saveDiscoveredVulnerabilities(scanResult);

        auditTrailService.log(
                currentUsername(),
                "URL_SCANNED",
                "Scanned " + normalized + " — " + scanResult.getFindings().size() + " findings",
                scanResult.getSeverityCount().getOrDefault("CRITICAL", 0) > 0 ? Severity.CRITICAL : Severity.MEDIUM,
                "SCAN",
                sourceIp,
                normalized);

        return scanResult;
    }

    private void saveDiscoveredVulnerabilities(VulnerabilityScanner.ScanResult scanResult) {
        for (VulnerabilityScanner.Finding finding : scanResult.getFindings()) {
            String cveId = generateCveId(finding);

            vulnerabilityRepository.findByCveId(cveId).ifPresentOrElse(
                    existing -> {
                        existing.setOccurrences(existing.getOccurrences() + 1);
                        existing.setLastDetected(LocalDateTime.now());
                        existing.setStatus(Status.OPEN);
                        vulnerabilityRepository.save(existing);
                    },
                    () -> {
                        Vulnerability vuln = new Vulnerability();
                        vuln.setCveId(cveId);
                        vuln.setTitle(finding.getTitle());
                        vuln.setDescription(finding.getDescription());
                        vuln.setSeverity(parseSeverity(finding.getSeverity()));
                        vuln.setStatus(Status.OPEN);
                        vuln.setCategory(finding.getCategory());
                        vuln.setOccurrences(1);
                        vuln.setAffectedEndpoints(1);
                        vuln.setThreatVector(finding.getType());
                        vuln.setRemediationAdvice(
                                finding.getRemediation() != null ? finding.getRemediation()
                                        : "Review and fix the identified issue.");
                        vuln.setLastDetected(LocalDateTime.now());
                        vulnerabilityRepository.save(vuln);
                    });
        }
    }

    private void updateTargetMetrics(AuditTarget target, VulnerabilityScanner.ScanResult scanResult) {
        target.setActiveFindings(scanResult.getFindings().size());

        int criticalCount = scanResult.getSeverityCount().getOrDefault("CRITICAL", 0);
        int highCount = scanResult.getSeverityCount().getOrDefault("HIGH", 0);
        int mediumCount = scanResult.getSeverityCount().getOrDefault("MEDIUM", 0);
        int lowCount = scanResult.getSeverityCount().getOrDefault("LOW", 0);

        double score = 100.0 - (criticalCount * 20 + highCount * 10 + mediumCount * 5 + lowCount * 2);
        target.setPostureScore((int) Math.round(Math.max(0, Math.min(100, score))));
        targetRepository.save(target);
    }

    private String generateCveId(VulnerabilityScanner.Finding finding) {
        String type = finding.getType().replaceAll("[^A-Z]", "");
        int hash = finding.getTitle().hashCode();
        return String.format("SCAN-%s-%05d", type, Math.abs(hash % 100000));
    }

    private Severity parseSeverity(String value) {
        try {
            return Severity.valueOf(value.toUpperCase());
        } catch (Exception ex) {
            return Severity.MEDIUM;
        }
    }

    private String normalizeUrl(String endpoint) {
        if (endpoint == null || endpoint.isBlank()) {
            throw new IllegalArgumentException("Target URL is required");
        }
        if (endpoint.startsWith("http://") || endpoint.startsWith("https://")) {
            return endpoint;
        }
        return "https://" + endpoint;
    }

    private String currentUsername() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {
            return principal.getUsername();
        }
        return "system";
    }
}
