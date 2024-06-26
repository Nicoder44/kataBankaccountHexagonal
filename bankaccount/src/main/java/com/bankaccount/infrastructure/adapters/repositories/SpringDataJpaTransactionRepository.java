package com.bankaccount.infrastructure.adapters.repositories;

import com.bankaccount.domain.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataJpaTransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<List<Transaction>> findByAccount_AccountNumberAndDateBetweenOrderByDateDesc(UUID accountNumber, Date startDate, Date endDate);
}

