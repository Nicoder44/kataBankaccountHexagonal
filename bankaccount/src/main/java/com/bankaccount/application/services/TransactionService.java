package com.bankaccount.application.services;

import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.Transaction;
import com.bankaccount.domain.services.TransactionDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionDomain transactionDomain;

    public Transaction createTransaction(BankAccount account, String type, double amount) {
        return transactionDomain.createTransaction(account, type, amount);
    }

    public List<Transaction> getTransactionsForAccount(UUID accountId, Date startDate, Date endDate) {
        return transactionDomain.getTransactionsForAccount(accountId, startDate, endDate);
    }
}