package com.cyberaudit.service;

import com.cyberaudit.model.SimulatedTarget;
import com.cyberaudit.repository.SimulatedTargetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SimulatedTargetService {
    
    private final SimulatedTargetRepository targetRepository;
    
    public SimulatedTargetService(SimulatedTargetRepository targetRepository) {
        this.targetRepository = targetRepository;
    }
    
    public SimulatedTarget createTarget(SimulatedTarget target) {
        return targetRepository.save(target);
    }
    
    @Transactional(readOnly = true)
    public Optional<SimulatedTarget> getTargetById(Long id) {
        return targetRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<SimulatedTarget> getTargetByName(String name) {
        return targetRepository.findByName(name);
    }
    
    @Transactional(readOnly = true)
    public List<SimulatedTarget> getTargetsByEnvironment(String environment) {
        return targetRepository.findByEnvironment(environment);
    }
    
    @Transactional(readOnly = true)
    public List<SimulatedTarget> getTargetsByType(String targetType) {
        return targetRepository.findByTargetType(targetType);
    }
    
    @Transactional(readOnly = true)
    public List<SimulatedTarget> getAllTargets() {
        return targetRepository.findAll();
    }
    
    public SimulatedTarget updateTarget(SimulatedTarget target) {
        return targetRepository.save(target);
    }
    
    public void deleteTarget(Long id) {
        targetRepository.deleteById(id);
    }
}
