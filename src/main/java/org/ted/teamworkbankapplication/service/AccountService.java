package org.ted.teamworkbankapplication.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.ted.teamworkbankapplication.enums.TransactionType;
import org.ted.teamworkbankapplication.model.BankAccount;
import org.ted.teamworkbankapplication.model.Transaction;
import org.ted.teamworkbankapplication.repository.primary.BankAccountRepository;
import org.ted.teamworkbankapplication.repository.primary.TransactionRepository;
import org.ted.teamworkbankapplication.repository.primary.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class AccountService {

    private BankAccountRepository accountRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    public AccountService(BankAccountRepository accountRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public BankAccount createAccount(BankAccount bankAccount) {
        return accountRepository.save(bankAccount);
    }

    public BankAccount deposit(BankAccount bankAccount, BigDecimal amount) {
        Transaction transaction = new Transaction();
        BankAccount edit = accountRepository.findById(bankAccount.getId()).orElseThrow();
        edit.setBalance(edit.getBalance().add(amount));

        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
        return accountRepository.save(edit);
    }

    public BankAccount withdraw(BankAccount bankAccount, BigDecimal amount) {
        Transaction transaction = new Transaction();
        BankAccount edit = accountRepository.findById(bankAccount.getId()).orElseThrow();
        if (amount.compareTo(edit.getBalance().subtract(amount)) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        edit.setBalance(edit.getBalance().subtract(amount));

        transaction.setAmount(transaction.getAmount().subtract(amount));
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
        return accountRepository.save(edit);


    }

    @Transactional
    public void transferMoney(long from, long to, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть положительной");
        }

        Transaction transaction = new Transaction();
        BankAccount fromAccount = accountRepository.findById(from).orElseThrow();
        BankAccount toAccount = accountRepository.findById(to).orElseThrow();

        // Проверка, что счета разные
        if (fromAccount.equals(toAccount)) {
            throw new IllegalArgumentException("Нельзя перевести деньги на тот же счёт");
        }

        // Проверка достаточности средств
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на счёте отправителя");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setToAccount(toAccount);
        transaction.setFromAccount(fromAccount);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

}
