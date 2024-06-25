package com.bankaccount.domain.services;

import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.Transaction;
import com.bankaccount.infrastructure.adapters.repositories.SpringDataJpaTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransactionDomain {

    private final SpringDataJpaTransactionRepository springDataJpaTransactionRepository;

    public Transaction createTransaction(BankAccount account, String type, double amount) {
        Transaction transaction = new Transaction(account, new Date(), type, amount);
        return springDataJpaTransactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsForAccount(UUID accountId, Date startDate, Date endDate) {
        return springDataJpaTransactionRepository.findByAccount_AccountNumberAndDateBetweenOrderByDateDesc(accountId, startDate, endDate);
    }
}
