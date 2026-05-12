package org.ted.teamworkbankapplication.repository.secondary;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class UserKnowledgeRepositoryImpl implements UserKnowledgeRepository {

    private final JdbcTemplate jdbcTemplate;

    private final Cache<String, Boolean> userOfCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    private final Cache<String, Boolean> activeUserOfCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    private final Cache<String, Boolean> transactionSumCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    private final Cache<String, Boolean> depositWithdrawCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();

    @Override
    public boolean isUserOf(UUID userId, String productType) {
        String key = userId + "_" + productType;
        return Boolean.TRUE.equals(userOfCache.get(key, k -> {
            String sql = """
                SELECT EXISTS (
                    SELECT 1 FROM transactions t
                    JOIN bank_accounts ba ON (t.from_account_id = ba.id OR t.to_account_id = ba.id)
                    JOIN users u ON ba.user_id = u.id
                    WHERE u.id = ? AND ba.type = ?
                    LIMIT 1
                )
            """;
            // Примечание: userId должен быть типа Long для соответствия users.id
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, userId, productType));
        }));
    }

    @Override
    public boolean isActiveUserOf(UUID userId, String productType) {
        String key = userId + "_" + productType;
        return Boolean.TRUE.equals(activeUserOfCache.get(key, k -> {
            String sql = """
                SELECT COUNT(*) >= 5 FROM transactions t
                JOIN bank_accounts ba ON (t.from_account_id = ba.id OR t.to_account_id = ba.id)
                JOIN users u ON ba.user_id = u.id
                WHERE u.id = ? AND ba.type = ?
            """;
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, userId, productType));
        }));
    }

    @Override
    public boolean compareTransactionSum(UUID userId, String productType,
                                         String transactionType, String operator, int constant) {
        String key = userId + "_" + productType + "_" + transactionType + "_" + operator + "_" + constant;
        return Boolean.TRUE.equals(transactionSumCache.get(key, k -> {
            String sql = """
                SELECT COALESCE(SUM(t.amount), 0) FROM transactions t
                JOIN bank_accounts ba ON (
                    (t.type = 'DEPOSIT' AND t.to_account_id = ba.id) OR
                    (t.type = 'WITHDRAW' AND t.from_account_id = ba.id)
                )
                JOIN users u ON ba.user_id = u.id
                WHERE u.id = ? AND ba.type = ? AND t.type = ?
            """;
            Long sum = jdbcTemplate.queryForObject(sql, Long.class, userId, productType, transactionType);

            long actualSum = (sum == null) ? 0L : sum;

            return switch (operator) {
                case ">" -> actualSum > constant;
                case "<" -> actualSum < constant;
                case ">=" -> actualSum >= constant;
                case "<=" -> actualSum <= constant;
                case "=" -> actualSum == constant;
                default -> throw new IllegalArgumentException("Некорректный оператор: " + operator);
            };
        }));
    }

    @Override
    public boolean compareDepositWithdraw(UUID userId, String productType, String operator) {
        String key = userId + "_" + productType + "_" + operator;
        return Boolean.TRUE.equals(depositWithdrawCache.get(key, k -> {
            // DEPOSIT сумма
            String depositSql = """
                SELECT COALESCE(SUM(t.amount), 0) FROM transactions t
                JOIN bank_accounts ba ON t.to_account_id = ba.id
                JOIN users u ON ba.user_id = u.id
                WHERE u.id = ? AND ba.type = ? AND t.type = 'DEPOSIT'
            """;

            // WITHDRAW сумма
            String withdrawSql = """
                SELECT COALESCE(SUM(t.amount), 0) FROM transactions t
                JOIN bank_accounts ba ON t.from_account_id = ba.id
                JOIN users u ON ba.user_id = u.id
                WHERE u.id = ? AND ba.type = ? AND t.type = 'WITHDRAW'
            """;

            Long depositSum = jdbcTemplate.queryForObject(depositSql, Long.class, userId, productType);
            Long withdrawSum = jdbcTemplate.queryForObject(withdrawSql, Long.class, userId, productType);

            long deposit = (depositSum == null) ? 0L : depositSum;
            long withdraw = (withdrawSum == null) ? 0L : withdrawSum;

            return switch (operator) {
                case ">" -> deposit > withdraw;
                case "<" -> deposit < withdraw;
                case ">=" -> deposit >= withdraw;
                case "<=" -> deposit <= withdraw;
                case "=" -> deposit == withdraw;
                default -> throw new IllegalArgumentException("Некорректный оператор: " + operator);
            };
        }));
    }
}