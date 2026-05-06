package org.ted.teamworkbankappliaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ted.teamworkbankappliaction.dto.DynamicRuleDto;
import org.ted.teamworkbankappliaction.dto.QueryConditionDto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DynamicRuleEvaluator {

    private final UserKnowledgeRepository userKnowledgeRepository;

    public boolean evaluate(DynamicRuleDto rule, UUID userId) {
        for (QueryConditionDto cond : rule.getRule()) {
            boolean result = evaluateCondition(cond, userId);
            if (Boolean.TRUE.equals(cond.getNegate())) {
                result = !result;
            }
            if (!result) {
                return false;
            }
        }
        return true;
    }

    private boolean evaluateCondition(QueryConditionDto cond, UUID userId) {
        return switch (cond.getQuery()) {
            case "USER_OF" ->
                    userKnowledgeRepository.isUserOf(userId, cond.getArguments().get(0));
            case "ACTIVE_USER_OF" ->
                    userKnowledgeRepository.isActiveUserOf(userId, cond.getArguments().get(0));
            case "TRANSACTION_SUM_COMPARE" -> {
                var args = cond.getArguments();
                yield userKnowledgeRepository.compareTransactionSum(
                        userId,
                        args.get(0), // productType
                        args.get(1), // transactionType
                        args.get(2), // operator
                        Integer.parseInt(args.get(3)) // constant
                );
            }
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> {
                var args = cond.getArguments();
                yield userKnowledgeRepository.compareDepositWithdraw(
                        userId,
                        args.get(0), // productType
                        args.get(1)  // operator
                );
            }
            default -> throw new IllegalArgumentException("неизвестный запрос: " + cond.getQuery());
        };
    }
}