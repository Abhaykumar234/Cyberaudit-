package com.cyberaudit.controller;

import com.cyberaudit.dto.MetricsDto;
import com.cyberaudit.service.MetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metrics")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class MetricsController {
    
    private final MetricsService metricsService;
    
    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }
    
    @GetMapping("/system")
    public ResponseEntity<MetricsDto> getSystemMetrics() {
        return ResponseEntity.ok(metricsService.getSystemMetrics());
    }
}
