package com.cyberaudit.service;

import com.cyberaudit.dto.AuditRequest;
import com.cyberaudit.dto.AuditResponse;
import com.cyberaudit.dto.FindingDto;
import com.cyberaudit.dto.LabResponse;
import com.cyberaudit.model.enums.Severity;
import com.cyberaudit.security.UserPrincipal;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ClaudeAiService {

    private static final Logger log = LoggerFactory.getLogger(ClaudeAiService.class);
    private static final String CLAUDE_API_URL = "https://api.anthropic.com/v1/messages";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AuditTrailService auditTrailService;

    @Value("${app.anthropic.api-key:}")
    private String apiKey;

    @Value("${app.anthropic.model}")
    private String model;

    public ClaudeAiService(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            AuditTrailService auditTrailService) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.auditTrailService = auditTrailService;
    }

    public AuditResponse generateAudit(AuditRequest request, String sourceIp) {
        LocalDateTime start = LocalDateTime.now();
        String prompt = buildAuditPrompt(request);
        String claudeText = callClaudeApi(prompt);
        AuditResponse response = parseAuditResponse(claudeText, request, start);

        String username = currentUsername();
        auditTrailService.log(username, "AUDIT_GENERATED",
                "Security audit completed for target " + request.getTargetId(),
                mapSeverity(response.getCriticalCount()), "AUDIT", sourceIp, request.getTargetId());

        return response;
    }

    public LabResponse evaluateLab(String mission, String codeSnippet, String language, String sourceIp) {
        String prompt = buildLabPrompt(mission, codeSnippet, language);
        String claudeText = callClaudeApi(prompt);
        LabResponse response = parseLabResponse(claudeText, mission);

        auditTrailService.log(currentUsername(), "LAB_EVALUATED",
                "Code analysis completed for mission: " + mission,
                Boolean.TRUE.equals(response.getIsVulnerable()) ? Severity.HIGH : Severity.LOW,
                "LAB", sourceIp, mission);

        return response;
    }

    private String buildAuditPrompt(AuditRequest request) {
        return """
                You are a security audit expert. Analyze the following target for vulnerabilities.

                Target ID: %s
                Audit Type: %s
                Scope: %s

                Provide a comprehensive security audit report. Respond with ONLY valid JSON (no markdown) using this schema:
                {
                  "findings": [
                    {
                      "id": "string",
                      "title": "string",
                      "severity": "CRITICAL|HIGH|MEDIUM|LOW",
                      "category": "string",
                      "description": "string",
                      "threatVector": "string",
                      "remediationAdvice": "string",
                      "occurrences": 0,
                      "affectedEndpoints": 0
                    }
                  ],
                  "criticalCount": 0,
                  "highCount": 0,
                  "summary": "string"
                }
                """.formatted(
                request.getTargetId(),
                request.getAuditType(),
                request.getScope() != null ? request.getScope() : "full");
    }

    private String buildLabPrompt(String mission, String codeSnippet, String language) {
        return """
                You are a secure coding expert. Analyze this code for vulnerabilities.

                Mission: %s
                Language: %s

                Code:
                ```
                %s
                ```

                Respond with ONLY valid JSON (no markdown) using this schema:
                {
                  "vulnerabilityAnalysis": "string",
                  "secureRepairLogic": "string",
                  "tacticalHints": "string",
                  "testSuite": "string",
                  "isVulnerable": true
                }
                """.formatted(mission, language != null ? language : "unknown", codeSnippet);
    }

    private String callClaudeApi(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "ANTHROPIC_API_KEY is not configured. Set the environment variable to enable AI analysis.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("max_tokens", 4096);
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(CLAUDE_API_URL, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode content = root.path("content");
            if (content.isArray()) {
                for (JsonNode block : content) {
                    if ("text".equals(block.path("type").asText())) {
                        return block.path("text").asText();
                    }
                }
            }
            throw new IllegalStateException("Unexpected response format from Claude API");
        } catch (IllegalStateException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Claude API call failed", ex);
            throw new IllegalStateException("AI analysis failed: " + ex.getMessage());
        }
    }

    private AuditResponse parseAuditResponse(String claudeText, AuditRequest request, LocalDateTime start) {
        try {
            JsonNode json = objectMapper.readTree(extractJson(claudeText));
            List<FindingDto> findings = new ArrayList<>();

            JsonNode findingsNode = json.path("findings");
            if (findingsNode.isArray()) {
                Iterator<JsonNode> it = findingsNode.elements();
                while (it.hasNext()) {
                    JsonNode node = it.next();
                    FindingDto finding = new FindingDto();
                    finding.setId(node.path("id").asText("UNKNOWN"));
                    finding.setTitle(node.path("title").asText("Untitled finding"));
                    finding.setSeverity(node.path("severity").asText("MEDIUM"));
                    finding.setCategory(node.path("category").asText("General"));
                    finding.setDescription(node.path("description").asText(""));
                    finding.setThreatVector(node.path("threatVector").asText(""));
                    finding.setRemediationAdvice(node.path("remediationAdvice").asText(""));
                    finding.setOccurrences(node.path("occurrences").asInt(0));
                    finding.setAffectedEndpoints(node.path("affectedEndpoints").asInt(0));
                    findings.add(finding);
                }
            }

            int criticalCount = json.path("criticalCount").asInt(countBySeverity(findings, "CRITICAL"));
            int highCount = json.path("highCount").asInt(countBySeverity(findings, "HIGH"));

            AuditResponse response = new AuditResponse();
            response.setTargetId(request.getTargetId());
            response.setAuditType(request.getAuditType());
            response.setStatus("COMPLETED");
            response.setStartTime(start);
            response.setEndTime(LocalDateTime.now());
            response.setFindingsCount(findings.size());
            response.setCriticalCount(criticalCount);
            response.setHighCount(highCount);
            response.setSummary(json.path("summary").asText("Audit completed"));
            response.setFindings(findings);
            return response;
        } catch (Exception ex) {
            log.error("Failed to parse audit response", ex);
            throw new IllegalStateException("Failed to parse AI audit response");
        }
    }

    private LabResponse parseLabResponse(String claudeText, String mission) {
        try {
            JsonNode json = objectMapper.readTree(extractJson(claudeText));
            LabResponse response = new LabResponse();
            response.setMissionId(UUID.randomUUID().toString());
            response.setStatus("COMPLETED");
            response.setVulnerabilityAnalysis(json.path("vulnerabilityAnalysis").asText(""));
            response.setSecureRepairLogic(json.path("secureRepairLogic").asText(""));
            response.setTacticalHints(json.path("tacticalHints").asText(""));
            response.setTestSuite(json.path("testSuite").asText(""));
            response.setIsVulnerable(json.path("isVulnerable").asBoolean(false));
            return response;
        } catch (Exception ex) {
            log.error("Failed to parse lab response for mission {}", mission, ex);
            throw new IllegalStateException("Failed to parse AI lab response");
        }
    }

    private String extractJson(String text) {
        if (text == null) {
            throw new IllegalStateException("Empty AI response");
        }
        String trimmed = text.trim();
        if (trimmed.startsWith("```")) {
            int start = trimmed.indexOf('\n');
            int end = trimmed.lastIndexOf("```");
            if (start >= 0 && end > start) {
                trimmed = trimmed.substring(start + 1, end).trim();
            }
        }
        int objectStart = trimmed.indexOf('{');
        int objectEnd = trimmed.lastIndexOf('}');
        if (objectStart >= 0 && objectEnd > objectStart) {
            return trimmed.substring(objectStart, objectEnd + 1);
        }
        return trimmed;
    }

    private int countBySeverity(List<FindingDto> findings, String severity) {
        return (int) findings.stream().filter(f -> severity.equalsIgnoreCase(f.getSeverity())).count();
    }

    private Severity mapSeverity(Integer criticalCount) {
        if (criticalCount != null && criticalCount > 0) {
            return Severity.CRITICAL;
        }
        return Severity.MEDIUM;
    }

    private String currentUsername() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {
            return principal.getUsername();
        }
        return "system";
    }
}
