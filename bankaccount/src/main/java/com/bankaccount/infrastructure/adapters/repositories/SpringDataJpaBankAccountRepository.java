package com.bankaccount.infrastructure.adapters.repositories;

import com.bankaccount.domain.models.BankAccount;

import java.util.Optional;
import java.util.UUID;

interface SpringDataJpaBankAccountRepository extends org.springframework.data.jpa.repository.JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByAccountNumber(UUID accountNumber);
}
