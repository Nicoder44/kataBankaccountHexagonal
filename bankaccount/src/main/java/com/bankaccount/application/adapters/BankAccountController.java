package com.bankaccount.application.adapters;


import com.bankaccount.application.services.BankAccountServicePorts;
import com.bankaccount.application.services.StatementServicePorts;
import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.LimitedBankAccount;
import com.bankaccount.domain.models.Statement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountServicePorts bankAccountService;

    private final StatementServicePorts statementService;

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
        return bankAccountService.setOverDraftLimit(accountNumber, amount);
    }

    @GetMapping("/{accountId}/statement")
    public Statement getMonthlyStatement(@PathVariable UUID accountId) {
        return statementService.generateMonthlyStatement(accountId);
    }

    @PostMapping(value = "/createLimitedAccount", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public LimitedBankAccount createAccount(@RequestBody LimitedBankAccount account) {
        return bankAccountService.createLimitedAccount(account.getBalance(), account.getDepositLimit());
    }
}
