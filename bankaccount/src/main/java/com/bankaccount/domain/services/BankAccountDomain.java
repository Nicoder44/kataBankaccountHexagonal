package com.bankaccount.domain.services;

import com.bankaccount.application.exceptions.BankAccountError;
import com.bankaccount.application.exceptions.BankAccountException;
import com.bankaccount.application.services.BankAccountServicePorts;
import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.LimitedBankAccount;
import com.bankaccount.domain.models.Transaction;
import com.bankaccount.domain.ports.out.BankAccountRepository;
import com.bankaccount.domain.ports.out.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BankAccountDomain implements BankAccountServicePorts {

    private final BankAccountRepository bankAccountRepository;

    private final TransactionRepository transactionRepository;
    @Override
    public BankAccount createAccount(double initialBalance, double overdraftLimit) {
        return bankAccountRepository.save(new BankAccount(initialBalance, overdraftLimit));
    }
    @Override
    public LimitedBankAccount createLimitedAccount(double initialBalance, double depositLimit) {
        return (LimitedBankAccount) bankAccountRepository.save(new LimitedBankAccount(initialBalance, depositLimit));
    }
    @Override
    public BankAccount getAccount(UUID accountNumber) {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new BankAccountException(BankAccountError.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return account;
    }
    @Override
    public BankAccount deposit(UUID accountNumber, double amount) {
        BankAccount account = getAccount(accountNumber);
        account.deposit(amount);

        Transaction transaction = new Transaction(account, new Date(), "DEPOSIT", amount);
        transactionRepository.save(transaction);
        return bankAccountRepository.save(account);
    }
    @Override
    public BankAccount withdraw(UUID accountNumber, double amount) {
        BankAccount account = getAccount(accountNumber);

        boolean result = account.withdraw(amount);
        if (result) {
            Transaction transaction = new Transaction(account, new Date(), "WITHDRAWAL", amount);
            transactionRepository.save(transaction);
            return bankAccountRepository.save(account);
        }
        throw new BankAccountException(BankAccountError.INSUFFICIENT_BALANCE, HttpStatus.BAD_REQUEST);

    }
    @Override
    public BankAccount setOverDraftLimit(UUID accountNumber, double amount) {
        BankAccount account = getAccount(accountNumber);
        if (amount < 0) {
            throw new BankAccountException(BankAccountError.INVALID_VALUE, HttpStatus.BAD_REQUEST);
        }
        account.setOverdraftLimit(amount);
        return bankAccountRepository.save(account);
    }
}
