package com.cyberaudit.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_targets")
public class AuditTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String environment;

    @Column(nullable = false)
    private String targetType;

    @Column(nullable = false)
    private String endpoint;

    @Column(nullable = false)
    private Integer postureScore;

    @Column(nullable = false)
    private Integer activeFindings;

    @Column(nullable = false)
    private Integer remediationRate;

    @Column(nullable = false)
    private Integer assetsInScope;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public AuditTarget() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public Integer getPostureScore() { return postureScore; }
    public void setPostureScore(Integer postureScore) { this.postureScore = postureScore; }

    public Integer getActiveFindings() { return activeFindings; }
    public void setActiveFindings(Integer activeFindings) { this.activeFindings = activeFindings; }

    public Integer getRemediationRate() { return remediationRate; }
    public void setRemediationRate(Integer remediationRate) { this.remediationRate = remediationRate; }

    public Integer getAssetsInScope() { return assetsInScope; }
    public void setAssetsInScope(Integer assetsInScope) { this.assetsInScope = assetsInScope; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (postureScore == null) postureScore = 100;
        if (activeFindings == null) activeFindings = 0;
        if (remediationRate == null) remediationRate = 100;
        if (assetsInScope == null) assetsInScope = 1;
    }
}
