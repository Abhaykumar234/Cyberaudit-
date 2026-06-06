package com.cyberaudit.controller;

import com.cyberaudit.dto.CreateTargetRequest;
import com.cyberaudit.model.AuditTarget;
import com.cyberaudit.service.AuditTargetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/targets")
public class TargetController {

    private final AuditTargetService targetService;

    public TargetController(AuditTargetService targetService) {
        this.targetService = targetService;
    }

    @GetMapping
    public ResponseEntity<List<AuditTarget>> getAllTargets() {
        return ResponseEntity.ok(targetService.getAllTargets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditTarget> getTargetById(@PathVariable Long id) {
        return targetService.getTargetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/environment/{environment}")
    public ResponseEntity<List<AuditTarget>> getTargetsByEnvironment(@PathVariable String environment) {
        return ResponseEntity.ok(targetService.getTargetsByEnvironment(environment));
    }

    @PostMapping
    public ResponseEntity<AuditTarget> createTarget(@Valid @RequestBody CreateTargetRequest request) {
        AuditTarget created = targetService.createTarget(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarget(@PathVariable Long id) {
        targetService.deleteTarget(id);
        return ResponseEntity.noContent().build();
    }
}
