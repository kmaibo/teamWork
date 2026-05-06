package org.ted.teamworkbankappliaction.service;

import org.springframework.stereotype.Repository;
import java.util.UUID;
@Repository
public class UserKnowledgeRepository {
public boolean isUserOf(UUID userId, String productType) {

    return false;
}

public boolean isActiveUserOf(UUID userId, String productType) {

    return false;
}

public boolean compareTransactionSum(UUID userId, String productType,
                                     String transactionType, String operator, int constant) {

    return false;
}

public boolean compareDepositWithdraw(UUID userId, String productType, String operator) {

    return false;
}
}