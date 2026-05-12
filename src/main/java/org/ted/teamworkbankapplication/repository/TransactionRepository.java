package org.ted.teamworkbankapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ted.teamworkbankapplication.model.BankAccount;
import org.ted.teamworkbankapplication.model.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountOrToAccount(BankAccount from, BankAccount to);
}
