package com.cyberaudit.service;

import com.cyberaudit.dto.CreateTargetRequest;
import com.cyberaudit.model.AuditTarget;
import com.cyberaudit.repository.AuditTargetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuditTargetService {

    private final AuditTargetRepository targetRepository;

    public AuditTargetService(AuditTargetRepository targetRepository) {
        this.targetRepository = targetRepository;
    }

    public AuditTarget createTarget(CreateTargetRequest request) {
        AuditTarget target = new AuditTarget();
        target.setName(request.getName().trim());
        target.setEnvironment(request.getEnvironment().trim());
        target.setTargetType(request.getTargetType().trim());
        target.setEndpoint(normalizeEndpoint(request.getEndpoint().trim()));
        target.setDescription(request.getDescription());
        target.setAssetsInScope(request.getAssetsInScope() != null ? request.getAssetsInScope() : 1);
        target.setPostureScore(100);
        target.setActiveFindings(0);
        target.setRemediationRate(100);
        return targetRepository.save(target);
    }

    @Transactional(readOnly = true)
    public Optional<AuditTarget> getTargetById(Long id) {
        return targetRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<AuditTarget> getTargetsByEnvironment(String environment) {
        return targetRepository.findByEnvironment(environment);
    }

    @Transactional(readOnly = true)
    public List<AuditTarget> getAllTargets() {
        return targetRepository.findAll();
    }

    public AuditTarget updateTargetMetrics(AuditTarget target) {
        return targetRepository.save(target);
    }

    public void deleteTarget(Long id) {
        if (!targetRepository.existsById(id)) {
            throw new IllegalArgumentException("Target not found: " + id);
        }
        targetRepository.deleteById(id);
    }

    private String normalizeEndpoint(String endpoint) {
        if (endpoint.startsWith("http://") || endpoint.startsWith("https://")) {
            return endpoint;
        }
        return "https://" + endpoint;
    }
}
