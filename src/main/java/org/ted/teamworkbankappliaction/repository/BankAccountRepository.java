package org.ted.teamworkbankappliaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ted.teamworkbankappliaction.model.BankAccount;
import org.ted.teamworkbankappliaction.model.User;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByUser(User user);
    Optional<BankAccount> findByAccountNumber(String accountNumber);

}
