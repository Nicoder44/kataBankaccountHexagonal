package com.bankaccount.application.services;

import com.bankaccount.domain.models.Statement;
import com.bankaccount.domain.services.StatementDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementDomain statementDomain;

    public Statement generateMonthlyStatement(UUID accountId) {
        return statementDomain.generateMonthlyStatement(accountId);
    }
}
