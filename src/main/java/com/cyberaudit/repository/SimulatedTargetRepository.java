package com.cyberaudit.repository;

import com.cyberaudit.model.SimulatedTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SimulatedTargetRepository extends JpaRepository<SimulatedTarget, Long> {
    
    Optional<SimulatedTarget> findByName(String name);
    
    List<SimulatedTarget> findByEnvironment(String environment);
    
    List<SimulatedTarget> findByTargetType(String targetType);
}
