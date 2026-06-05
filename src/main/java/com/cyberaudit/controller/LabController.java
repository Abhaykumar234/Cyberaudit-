package com.cyberaudit.controller;

import com.cyberaudit.dto.LabRequest;
import com.cyberaudit.dto.LabResponse;
import com.cyberaudit.service.ClaudeAiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lab")
public class LabController {

    private final ClaudeAiService claudeAiService;

    public LabController(ClaudeAiService claudeAiService) {
        this.claudeAiService = claudeAiService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<LabResponse> evaluateLab(
            @Valid @RequestBody LabRequest request,
            HttpServletRequest httpRequest) {
        LabResponse response = claudeAiService.evaluateLab(
                request.getMission(),
                request.getCodeSnippet(),
                request.getLanguage(),
                resolveClientIp(httpRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Lab service is running");
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
