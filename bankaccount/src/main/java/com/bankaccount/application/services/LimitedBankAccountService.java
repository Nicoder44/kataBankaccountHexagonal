package com.bankaccount.application.services;

import com.bankaccount.domain.models.LimitedBankAccount;
import com.bankaccount.domain.services.BankAccountDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LimitedBankAccountService {

    private final BankAccountDomain bankAccountDomain;

    public LimitedBankAccount createAccount(double initialBalance, double depositLimit) {
        return bankAccountDomain.createLimitedAccount(initialBalance, depositLimit);
    }
}
