package com.bankaccount.domain.ports.out;

import com.bankaccount.domain.models.Transaction;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository {

    Transaction save(Transaction transaction);
    List<Transaction> findByAccount_AccountNumberAndDateBetweenOrderByDateDesc(UUID accountNumber, Date startDate, Date endDate);
}
