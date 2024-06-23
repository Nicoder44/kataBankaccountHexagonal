package com.bankaccount.application.services;

import com.bankaccount.domain.models.LimitedBankAccount;
import com.bankaccount.domain.services.BankAccountDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LimitedBankAccountService {

    @Autowired
    private BankAccountDomain bankAccountDomain;

    public LimitedBankAccount createAccount(double initialBalance, double depositLimit) {
        return bankAccountDomain.createLimitedAccount(initialBalance, depositLimit);
    }
}
