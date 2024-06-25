package com.bankaccount.application.services;

import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.services.BankAccountDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountDomain bankAccountDomain;

    public BankAccount createAccount(double initialBalance, double overdraftLimit) {
        return bankAccountDomain.createAccount(initialBalance, overdraftLimit);
    }

    public BankAccount getAccount(UUID accountNumber) {
        return bankAccountDomain.getAccount(accountNumber);
    }

    public BankAccount deposit(UUID accountNumber, double amount) {
        return bankAccountDomain.deposit(accountNumber, amount);
    }

    public BankAccount withdraw(UUID accountNumber, double amount) {
        return bankAccountDomain.withdraw(accountNumber, amount);
    }

    public BankAccount setOverdraftLimit(UUID accountNumber, double amount) {
        return bankAccountDomain.setOverDraftLimit(accountNumber, amount);
    }
}
