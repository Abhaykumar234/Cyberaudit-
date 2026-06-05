package com.cyberaudit.controller;

import com.cyberaudit.scanner.VulnerabilityScanner;
import com.cyberaudit.service.RealTimeScanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/scan")
public class ScanController {

    private final RealTimeScanService scanService;

    public ScanController(RealTimeScanService scanService) {
        this.scanService = scanService;
    }

    @PostMapping("/target/{targetId}")
    public CompletableFuture<ResponseEntity<VulnerabilityScanner.ScanResult>> scanTarget(
            @PathVariable Long targetId) {
        return scanService.scanTargetAsync(targetId)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/url")
    public ResponseEntity<VulnerabilityScanner.ScanResult> scanUrl(@RequestBody ScanRequest request) {
        return ResponseEntity.ok(scanService.scanTargetSync(request.getUrl()));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getScannerStatus() {
        return ResponseEntity.ok(Map.of(
                "status", "ONLINE",
                "version", "1.0.0",
                "capabilities", "port_scan,header_check,ssl_check,path_discovery"));
    }

    public static class ScanRequest {
        private String url;

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }
}
