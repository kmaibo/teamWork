package org.ted.teamworkbankappliaction.controller;

import org.springframework.web.bind.annotation.*;
import org.ted.teamworkbankappliaction.model.BankAccount;
import org.ted.teamworkbankappliaction.repository.BankAccountRepository;
import org.ted.teamworkbankappliaction.service.AccountService;


import java.math.BigDecimal;

@RestController
@RequestMapping("/bank_account")
public class AccountController {

    private BankAccountRepository accountRepository;
    private AccountService accountService;

    public AccountController(BankAccountRepository accountRepository, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public BankAccount getAccount(@PathVariable long id) {
        return accountRepository.findById(id).get();
    }

    @PostMapping
    public BankAccount createAccount(@RequestBody BankAccount account) {
        return accountRepository.save(account);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable long id) {
        accountRepository.deleteById(id);
    }

    @PatchMapping
    public BankAccount deposit(@RequestBody BankAccount account, @RequestBody BigDecimal amount) {
        return accountService.deposit(account, amount);
    }

    @PatchMapping
    public BankAccount withdraw(@RequestBody BankAccount account, @RequestBody BigDecimal amount) {
        return accountService.withdraw(account, amount);
    }

    @PatchMapping
    public void transferMoney(@RequestBody BankAccount from,@RequestBody BankAccount to,@RequestBody BigDecimal amount) {
        accountService.transferMoney(from,to,amount);
    }
}
