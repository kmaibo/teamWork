package org.ted.teamworkbankapplication.repository.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ted.teamworkbankapplication.model.RuleEntity;
import org.ted.teamworkbankapplication.model.RuleStat;

import java.util.Optional;
import java.util.UUID;

public interface RuleStatRepository extends JpaRepository<RuleStat, Long> {
    Optional<RuleStat> findByRuleId(UUID ruleId);

    void deleteByRule(RuleEntity rule);

}
