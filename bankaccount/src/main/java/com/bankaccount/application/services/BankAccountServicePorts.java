package com.bankaccount.application.services;

import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.LimitedBankAccount;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface BankAccountServicePorts {

    BankAccount createAccount(double initialBalance, double overdraftLimit);

    BankAccount getAccount(UUID accountNumber);

    BankAccount deposit(UUID accountNumber, double amount);

    BankAccount withdraw(UUID accountNumber, double amount);

    BankAccount setOverDraftLimit(UUID accountNumber, double amount);

    LimitedBankAccount createLimitedAccount(double initialBalance, double depositLimit);
}
