package org.ted.teamworkbankappliaction.service;

import jakarta.transaction.Transactional;
import org.hibernate.validator.internal.constraintvalidators.bv.money.CurrencyValidatorForMonetaryAmount;
import org.springframework.stereotype.Service;
import org.ted.teamworkbankappliaction.enums.TransactionType;
import org.ted.teamworkbankappliaction.model.BankAccount;
import org.ted.teamworkbankappliaction.model.Transaction;
import org.ted.teamworkbankappliaction.repository.BankAccountRepository;
import org.ted.teamworkbankappliaction.repository.TransactionRepository;
import org.ted.teamworkbankappliaction.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class AccountService {

    private final CurrencyValidatorForMonetaryAmount currencyValidatorForMonetaryAmount;
    private BankAccountRepository accountRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    public AccountService(BankAccountRepository accountRepository, UserRepository userRepository, TransactionRepository transactionRepository, CurrencyValidatorForMonetaryAmount currencyValidatorForMonetaryAmount) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.currencyValidatorForMonetaryAmount = currencyValidatorForMonetaryAmount;
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
        edit.setBalance(edit.getBalance().subtract(amount));

        transaction.setAmount(transaction.getAmount().subtract(amount));
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
        return accountRepository.save(edit);


    }

    public void transferMoney(BankAccount from, BankAccount to, BigDecimal amount) {
        Transaction transaction = new Transaction();
        BankAccount fromAccount = accountRepository.findById(from.getId()).orElseThrow();
        BankAccount toAccount = accountRepository.findById(to.getId()).orElseThrow();

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
