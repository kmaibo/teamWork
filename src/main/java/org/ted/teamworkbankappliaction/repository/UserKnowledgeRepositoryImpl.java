package org.ted.teamworkbankappliaction.repository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository // ОБЯЗАТЕЛЬНО ДОЛЖНО БЫТЬ ТУТ
@RequiredArgsConstructor
public class UserKnowledgeRepositoryImpl implements UserKnowledgeRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean isUserOf(UUID userId, String productType) {
        return false;
    }

    @Override
    public boolean isActiveUserOf(UUID userId, String productType) {
        return false;
    }

    @Override
    public boolean compareTransactionSum(UUID userId, String productType, String transactionType, String operator, int constant) {
        return false;
    }

    @Override
    public boolean compareDepositWithdraw(UUID userId, String productType, String operator) {
        return false;
    }
}
