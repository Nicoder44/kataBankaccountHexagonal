package com.bankaccount.application.services;

import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.Transaction;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public interface TransactionServicePorts {

    Transaction createTransaction(BankAccount account, String type, double amount);

    List<Transaction> getTransactionsForAccount(UUID accountId, Date startDate, Date endDate);

}