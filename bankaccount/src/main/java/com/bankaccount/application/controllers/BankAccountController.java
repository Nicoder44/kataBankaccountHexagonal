package com.bankaccount.application.controllers;


import com.bankaccount.application.services.BankAccountService;
import com.bankaccount.application.services.StatementService;
import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.Statement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    private final StatementService statementService;

    @PostMapping("/createAccount")
    public BankAccount createAccount(@RequestBody(required = false) BankAccount account) {
        return bankAccountService.createAccount(account.getBalance(), account.getOverdraftLimit());
    }

    @GetMapping("/{accountNumber}")
    public BankAccount getAccount(@PathVariable UUID accountNumber) {
        return bankAccountService.getAccount(accountNumber);
    }

    @PostMapping("/{accountNumber}/deposit")
    public BankAccount deposit(@PathVariable UUID accountNumber, @RequestParam double amount) {
        return bankAccountService.deposit(accountNumber, amount);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public BankAccount withdraw(@PathVariable UUID accountNumber, @RequestParam double amount) {
        return bankAccountService.withdraw(accountNumber, amount);
    }

    @PostMapping("/{accountNumber}/setOverDraftLimit")
    public BankAccount setOverDraftLimit(@PathVariable UUID accountNumber, @RequestParam double amount) {
        return bankAccountService.setOverdraftLimit(accountNumber, amount);
    }

    @GetMapping("/{accountId}/statement")
    public Statement getMonthlyStatement(@PathVariable UUID accountId) {
        return statementService.generateMonthlyStatement(accountId);
    }
}
