package com.bankaccount.domain.ports;

import com.bankaccount.domain.models.BankAccount;

import java.util.UUID;

public interface BankAccountRepository {
    BankAccount save(BankAccount account);
    BankAccount findByAccountNumber(UUID accountNumber);
}
