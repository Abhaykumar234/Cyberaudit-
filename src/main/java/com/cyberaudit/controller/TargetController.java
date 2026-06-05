package com.cyberaudit.controller;

import com.cyberaudit.model.SimulatedTarget;
import com.cyberaudit.service.SimulatedTargetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/targets")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class TargetController {
    
    private final SimulatedTargetService targetService;
    
    public TargetController(SimulatedTargetService targetService) {
        this.targetService = targetService;
    }
    
    @GetMapping
    public ResponseEntity<List<SimulatedTarget>> getAllTargets() {
        return ResponseEntity.ok(targetService.getAllTargets());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SimulatedTarget> getTargetById(@PathVariable Long id) {
        return targetService.getTargetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/environment/{environment}")
    public ResponseEntity<List<SimulatedTarget>> getTargetsByEnvironment(
            @PathVariable String environment) {
        return ResponseEntity.ok(targetService.getTargetsByEnvironment(environment));
    }
    
    @PostMapping
    public ResponseEntity<SimulatedTarget> createTarget(@RequestBody SimulatedTarget target) {
        SimulatedTarget created = targetService.createTarget(target);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SimulatedTarget> updateTarget(
            @PathVariable Long id,
            @RequestBody SimulatedTarget target) {
        target.setId(id);
        return ResponseEntity.ok(targetService.updateTarget(target));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarget(@PathVariable Long id) {
        targetService.deleteTarget(id);
        return ResponseEntity.noContent().build();
    }
}
