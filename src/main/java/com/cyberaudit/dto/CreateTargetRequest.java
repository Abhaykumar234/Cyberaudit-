package com.cyberaudit.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTargetRequest {

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String environment;

    @NotBlank
    @Size(max = 50)
    private String targetType;

    @NotBlank
    @Size(max = 500)
    private String endpoint;

    @Size(max = 2000)
    private String description;

    @Min(1)
    @Max(10000)
    private Integer assetsInScope = 1;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getAssetsInScope() { return assetsInScope; }
    public void setAssetsInScope(Integer assetsInScope) { this.assetsInScope = assetsInScope; }
}
