package com.cyberaudit.controller;

import com.cyberaudit.scanner.VulnerabilityScanner;
import com.cyberaudit.service.RealTimeScanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/scan")
@Validated
public class ScanController {

    private final RealTimeScanService scanService;

    public ScanController(RealTimeScanService scanService) {
        this.scanService = scanService;
    }

    @PostMapping("/target/{targetId}")
    public CompletableFuture<ResponseEntity<VulnerabilityScanner.ScanResult>> scanTarget(
            @PathVariable Long targetId,
            HttpServletRequest httpRequest) {
        return scanService.scanTargetAsync(targetId, resolveClientIp(httpRequest))
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/url")
    public ResponseEntity<VulnerabilityScanner.ScanResult> scanUrl(
            @Valid @RequestBody ScanRequest request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(scanService.scanUrlSync(request.getUrl(), resolveClientIp(httpRequest)));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getScannerStatus() {
        return ResponseEntity.ok(Map.of(
                "status", "ONLINE",
                "version", "1.0.0",
                "capabilities", "port_scan,header_check,ssl_check,path_discovery"));
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    public static class ScanRequest {
        @NotBlank
        private String url;

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }
}
