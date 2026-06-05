package com.cyberaudit.service;

import com.cyberaudit.dto.MetricsDto;
import com.cyberaudit.model.enums.Severity;
import com.cyberaudit.model.enums.Status;
import com.cyberaudit.repository.SimulatedTargetRepository;
import com.cyberaudit.repository.VulnerabilityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MetricsService {

    private final VulnerabilityRepository vulnerabilityRepository;
    private final SimulatedTargetRepository targetRepository;

    public MetricsService(
            VulnerabilityRepository vulnerabilityRepository,
            SimulatedTargetRepository targetRepository) {
        this.vulnerabilityRepository = vulnerabilityRepository;
        this.targetRepository = targetRepository;
    }

    public MetricsDto getSystemMetrics() {
        Long criticalCount = vulnerabilityRepository.countBySeverity(Severity.CRITICAL);
        Long highCount = vulnerabilityRepository.countBySeverity(Severity.HIGH);
        Long mediumCount = vulnerabilityRepository.countBySeverity(Severity.MEDIUM);
        Long totalCount = vulnerabilityRepository.count();
        Long openCount = vulnerabilityRepository.countByStatus(Status.OPEN);
        Long resolvedCount = vulnerabilityRepository.countByStatus(Status.RESOLVED);
        Long closedCount = vulnerabilityRepository.countByStatus(Status.CLOSED);
        long remediatedCount = resolvedCount + closedCount;

        double postureScore = calculatePostureScore(criticalCount, highCount, totalCount);
        String threatLevel = determineThreatLevel(criticalCount, highCount);

        int remediationRate = totalCount == 0 ? 100
                : (int) Math.round((remediatedCount * 100.0) / totalCount);

        int assetsInScope = (int) targetRepository.count();
        int activeEndpoints = targetRepository.findAll().stream()
                .mapToInt(t -> t.getAssetsInScope() != null ? t.getAssetsInScope() : 0)
                .sum();

        MetricsDto metrics = new MetricsDto();
        metrics.setPostureScore(postureScore);
        metrics.setActiveFindings(Math.toIntExact(openCount));
        metrics.setCriticalCount(Math.toIntExact(criticalCount));
        metrics.setHighCount(Math.toIntExact(highCount));
        metrics.setMediumCount(Math.toIntExact(mediumCount));
        metrics.setRemediationRate(remediationRate);
        metrics.setAssetsInScope(assetsInScope);
        metrics.setGlobalThreatLevel(threatLevel);
        metrics.setActiveEndpoints(activeEndpoints);
        metrics.setEngineStatus("Online");
        metrics.setTotalFindings(Math.toIntExact(totalCount));

        return metrics;
    }

    private double calculatePostureScore(Long critical, Long high, Long total) {
        if (total == 0) {
            return 100.0;
        }
        double criticalWeight = critical * 10.0;
        double highWeight = high * 5.0;
        double totalImpact = criticalWeight + highWeight;
        return Math.max(0, 100.0 - (totalImpact / total));
    }

    private String determineThreatLevel(Long critical, Long high) {
        if (critical > 5) return "CRITICAL";
        if (critical > 0 || high > 10) return "ELEVATED";
        if (high > 0) return "MODERATE";
        return "LOW";
    }
}
