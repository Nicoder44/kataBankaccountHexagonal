package com.bankaccount.infrastructure.adapters.repositories;

import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.ports.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaBankAccountRepository implements BankAccountRepository {

    @Autowired
    private SpringDataJpaBankAccountRepository springDataJpaBankAccountRepository;

    @Override
    public BankAccount save (BankAccount account) {
        return springDataJpaBankAccountRepository.save(account);
    }

    @Override
    public BankAccount findByAccountNumber (UUID accountNumber) {
        Optional<BankAccount> account = springDataJpaBankAccountRepository.findByAccountNumber(accountNumber);
        return account.orElse(null);
    }
}