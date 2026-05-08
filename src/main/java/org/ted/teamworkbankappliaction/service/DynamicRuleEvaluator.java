package org.ted.teamworkbankappliaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ted.teamworkbankappliaction.dto.DynamicRuleDto;
import org.ted.teamworkbankappliaction.repository.UserKnowledgeRepository; // Проверьте импорт

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DynamicRuleEvaluator {

    private final UserKnowledgeRepository userKnowledgeRepository;

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
        return true;
    }
    private boolean evaluateCondition(UUID userId, DynamicRuleDto.QueryConditionDto cond) {
        return switch (cond.getQuery()) {
            case "USER_OF" ->
                    userKnowledgeRepository.isUserOf(userId, cond.getArguments().get(0));
            case "ACTIVE_USER_OF" ->
                    userKnowledgeRepository.isActiveUserOf(userId, cond.getArguments().get(0));
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