package com.cyberaudit.repository;

import com.cyberaudit.model.AuditTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuditTargetRepository extends JpaRepository<AuditTarget, Long> {

    Optional<AuditTarget> findByName(String name);

    List<AuditTarget> findByEnvironment(String environment);

    List<AuditTarget> findByTargetType(String targetType);
}
