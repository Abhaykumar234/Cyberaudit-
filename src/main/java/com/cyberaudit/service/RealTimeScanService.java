package com.cyberaudit.service;

import com.cyberaudit.model.SimulatedTarget;
import com.cyberaudit.model.Vulnerability;
import com.cyberaudit.model.enums.Severity;
import com.cyberaudit.model.enums.Status;
import com.cyberaudit.repository.SimulatedTargetRepository;
import com.cyberaudit.repository.VulnerabilityRepository;
import com.cyberaudit.scanner.VulnerabilityScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class RealTimeScanService {

    private static final Logger logger = LoggerFactory.getLogger(RealTimeScanService.class);

    private final VulnerabilityScanner vulnerabilityScanner;
    private final SimulatedTargetRepository targetRepository;
    private final VulnerabilityRepository vulnerabilityRepository;

    public RealTimeScanService(
            VulnerabilityScanner vulnerabilityScanner,
            SimulatedTargetRepository targetRepository,
            VulnerabilityRepository vulnerabilityRepository) {
        this.vulnerabilityScanner = vulnerabilityScanner;
        this.targetRepository = targetRepository;
        this.vulnerabilityRepository = vulnerabilityRepository;
    }

    @Async
    public CompletableFuture<VulnerabilityScanner.ScanResult> scanTargetAsync(Long targetId) {
        logger.info("Starting async scan for target ID: {}", targetId);

        SimulatedTarget target = targetRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("Target not found: " + targetId));

        String scanUrl = normalizeUrl(target.getEndpoint());
        VulnerabilityScanner.ScanResult scanResult = vulnerabilityScanner.scanTarget(scanUrl);

        saveDiscoveredVulnerabilities(scanResult);
        updateTargetMetrics(target, scanResult);

        logger.info("Completed scan for target: {}. Found {} issues", target.getName(),
                scanResult.getFindings().size());

        return CompletableFuture.completedFuture(scanResult);
    }

    public VulnerabilityScanner.ScanResult scanTargetSync(String targetUrl) {
        logger.info("Starting synchronous scan for URL: {}", targetUrl);
        return vulnerabilityScanner.scanTarget(normalizeUrl(targetUrl));
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

    private void updateTargetMetrics(SimulatedTarget target, VulnerabilityScanner.ScanResult scanResult) {
        int activeFindings = scanResult.getFindings().size();
        target.setActiveFindings(activeFindings);

        int criticalCount = scanResult.getSeverityCount().getOrDefault("CRITICAL", 0);
        int highCount = scanResult.getSeverityCount().getOrDefault("HIGH", 0);
        int mediumCount = scanResult.getSeverityCount().getOrDefault("MEDIUM", 0);
        int lowCount = scanResult.getSeverityCount().getOrDefault("LOW", 0);

        double score = 100.0 - (criticalCount * 20 + highCount * 10 + mediumCount * 5 + lowCount * 2);
        score = Math.max(0, Math.min(100, score));

        target.setPostureScore((int) Math.round(score));
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
            throw new IllegalArgumentException("Target endpoint is required");
        }
        if (endpoint.startsWith("http://") || endpoint.startsWith("https://")) {
            return endpoint;
        }
        return "https://" + endpoint;
    }
}
