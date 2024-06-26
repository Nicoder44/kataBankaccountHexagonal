package com.bankaccount.infrastructure.adapters.repositories;

import com.bankaccount.domain.models.Transaction;
import com.bankaccount.domain.ports.out.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaTransactionRepository implements TransactionRepository {

    private final SpringDataJpaTransactionRepository springDataJpaTransactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
        return springDataJpaTransactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findByAccount_AccountNumberAndDateBetweenOrderByDateDesc(UUID accountNumber, Date startDate, Date endDate) {
        Optional<List<Transaction>> transactions = springDataJpaTransactionRepository.findByAccount_AccountNumberAndDateBetweenOrderByDateDesc(
                accountNumber,
                startDate,
                endDate
        );
        return transactions.orElse(null);
    }
}
