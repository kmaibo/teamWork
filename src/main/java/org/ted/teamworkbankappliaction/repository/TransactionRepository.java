package org.ted.teamworkbankappliaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ted.teamworkbankappliaction.model.BankAccount;
import org.ted.teamworkbankappliaction.model.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountOrToAccount(BankAccount from, BankAccount to);
}
