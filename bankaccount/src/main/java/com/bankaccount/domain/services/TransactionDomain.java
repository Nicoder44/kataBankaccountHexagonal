package com.bankaccount.domain.services;

import com.bankaccount.application.services.TransactionServicePorts;
import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.Transaction;
import com.bankaccount.domain.ports.out.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransactionDomain implements TransactionServicePorts {

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(BankAccount account, String type, double amount) {
        Transaction transaction = new Transaction(account, new Date(), type, amount);
        return transactionRepository.save(transaction);
    }
    @Override
    public List<Transaction> getTransactionsForAccount(UUID accountId, Date startDate, Date endDate) {
        return transactionRepository.findByAccount_AccountNumberAndDateBetweenOrderByDateDesc(accountId, startDate, endDate);
    }
}
