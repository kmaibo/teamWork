package org.ted.teamworkbankappliaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ted.teamworkbankappliaction.model.RuleEntity;

import java.util.Optional;
import java.util.UUID;

public interface DynamicRuleRepository extends JpaRepository<RuleEntity, UUID> {
    void deleteByProductId(UUID productId);
    Optional<RuleEntity> findByProductId(UUID productId);
    boolean existsByProductId(UUID productId);
}