package org.ted.teamworkbankapplication.repository;

import java.util.UUID;

public interface UserKnowledgeRepository {

    // Проверка: есть ли у пользователя хотя бы одна транзакция по типу продукта
    boolean isUserOf(UUID userId, String productType);

    // Проверка: есть ли у пользователя минимум 5 транзакций по типу продукта
    boolean isActiveUserOf(UUID userId, String productType);

    // Сравнение суммы транзакций конкретного типа с константой
    boolean compareTransactionSum(UUID userId, String productType, String transactionType, String operator, int constant);

    // Сравнение суммы пополнений с суммой списаний
    boolean compareDepositWithdraw(UUID userId, String productType, String operator);
}