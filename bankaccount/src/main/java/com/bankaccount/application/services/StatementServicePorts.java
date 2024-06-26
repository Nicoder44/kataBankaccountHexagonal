package com.bankaccount.application.services;

import com.bankaccount.domain.models.Statement;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface StatementServicePorts {
    Statement generateMonthlyStatement(UUID accountId);
}
