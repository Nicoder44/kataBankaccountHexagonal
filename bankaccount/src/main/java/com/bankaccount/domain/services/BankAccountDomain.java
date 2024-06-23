package com.bankaccount.domain.services;

import com.bankaccount.application.exceptions.BankAccountError;
import com.bankaccount.application.exceptions.BankAccountException;
import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.ports.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BankAccountDomain {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public BankAccount createAccount(double initialBalance, double overdraftLimit) {
        return bankAccountRepository.save(new BankAccount(initialBalance, overdraftLimit));
    }

    public BankAccount getAccount(UUID accountNumber) {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new BankAccountException(BankAccountError.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return account;
    }

    public BankAccount deposit(UUID accountNumber, double amount) {
        BankAccount account = getAccount(accountNumber);
        account.deposit(amount);
        return bankAccountRepository.save(account);
    }

    public BankAccount withdraw(UUID accountNumber, double amount) {
        BankAccount account = getAccount(accountNumber);

        boolean result = account.withdraw(amount);
        if (result) {
            return bankAccountRepository.save(account);
        }
        throw new BankAccountException(BankAccountError.INSUFFICIENT_BALANCE, HttpStatus.BAD_REQUEST);

    }

    public BankAccount setOverDraftLimit(UUID accountNumber, double amount) {
        BankAccount account = getAccount(accountNumber);
        if(amount < 0){
            throw new BankAccountException(BankAccountError.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
        account.setOverdraftLimit(amount);
        return bankAccountRepository.save(account);
    }
}
