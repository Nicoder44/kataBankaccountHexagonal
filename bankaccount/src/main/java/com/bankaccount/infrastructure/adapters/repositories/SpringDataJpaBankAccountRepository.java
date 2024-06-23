package com.bankaccount.infrastructure.adapters.repositories;

import com.bankaccount.domain.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface SpringDataJpaBankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByAccountNumber(UUID accountNumber);
}
