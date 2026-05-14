package org.ted.teamworkbankapplication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ted.teamworkbankapplication.dto.DynamicRuleDto;
import org.ted.teamworkbankapplication.model.RuleEntity;
import org.ted.teamworkbankapplication.model.RuleStat;
import org.ted.teamworkbankapplication.repository.secondary.DynamicRuleRepository;
import org.ted.teamworkbankapplication.repository.secondary.RuleStatRepository;
import org.ted.teamworkbankapplication.repository.secondary.UserKnowledgeRepository; // Проверьте импорт

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DynamicRuleEvaluator {

    private final UserKnowledgeRepository userKnowledgeRepository;
    private final RuleStatRepository ruleStatRepository;
    private final DynamicRuleRepository dynamicRuleRepository;

    public boolean evaluate(DynamicRuleDto rule, UUID userId) {
        if (rule.getRule() == null) return true;

        for (DynamicRuleDto.QueryConditionDto cond : rule.getRule()) {
            boolean result = evaluateCondition(userId, cond);
            if (cond.isNegate()) {
                result = !result;
            }

            if (!result) {
                return false;
            }
        }
        if (rule.getId() != null) {
            try {
                RuleStat stat = ruleStatRepository.findByRuleId(rule.getId()).orElseGet(() -> {
                    RuleEntity ruleEntity = (RuleEntity) dynamicRuleRepository.findById(rule.getId()).orElseThrow(() ->
                            new RuntimeException("Правило не найдено"));
                    return new RuleStat(ruleEntity);
                });
                stat.setCount(stat.getCount() + 1);
                ruleStatRepository.save(stat);
            } catch (Exception e) {
                System.err.println("Не удалось обновить статистику правил: " + e.getMessage());
            }
        }
        return true;
    }

    private boolean evaluateCondition(UUID userId, DynamicRuleDto.QueryConditionDto cond) {
        return switch (cond.getQuery()) {
            case "USER_OF" -> userKnowledgeRepository.isUserOf(userId, cond.getArguments().get(0));
            case "ACTIVE_USER_OF" -> userKnowledgeRepository.isActiveUserOf(userId, cond.getArguments().get(0));
            case "TRANSACTION_SUM_COMPARE" -> {
                var args = cond.getArguments();
                yield userKnowledgeRepository.compareTransactionSum(
                        userId,
                        args.get(0),
                        args.get(1),
                        args.get(2),
                        Integer.parseInt(args.get(3))
                );
            }
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> {
                var args = cond.getArguments();
                yield userKnowledgeRepository.compareDepositWithdraw(
                        userId,
                        args.get(0),
                        args.get(1)
                );
            }
            default -> throw new IllegalArgumentException("неизвестный запрос: " + cond.getQuery());
        };
    }
}