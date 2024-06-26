package com.bankaccount.domain.services;

import com.bankaccount.application.services.BankAccountServicePorts;
import com.bankaccount.application.services.TransactionServicePorts;
import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.Statement;
import com.bankaccount.domain.models.Transaction;
import com.bankaccount.domain.ports.out.BankAccountRepository;
import com.bankaccount.domain.ports.out.TransactionRepository;
import com.bankaccount.infrastructure.adapters.repositories.JpaBankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatementDomainTest {

    @Mock
    private BankAccountRepository bankAccountService;
    @Mock
    private TransactionRepository transactionService;
    @InjectMocks
    private StatementDomain statementDomain;

    private BankAccount mockAccount;
    private List<Transaction> mockTransactions;

    @BeforeEach
    void setup() {
        mockAccount = new BankAccount(0, 1000.0);
        mockTransactions = Arrays.asList(
                new Transaction(mockAccount, new Date(), "DEPOSIT", 100.0),
                new Transaction(mockAccount, new Date(), "WITHDRAWAL", 50.0)
        );
    }

    @Test
    void testGenerateMonthlyStatement() {
        // Given
        when(bankAccountService.findByAccountNumber(any())).thenReturn(mockAccount);
        when(transactionService.findByAccount_AccountNumberAndDateBetweenOrderByDateDesc(any(), any(), any()))
                .thenReturn(mockTransactions);

        // When
        Statement statement = statementDomain.generateMonthlyStatement(mockAccount.getAccountNumber());

        // Then
        assertEquals("Compte Courant", statement.getAccountType());
        assertEquals(0, statement.getBalance());
        assertEquals(2, statement.getNumberOfTransactionsThisMonth());
        assertEquals(mockTransactions, statement.getTransactions());

        verify(bankAccountService, times(1)).findByAccountNumber(mockAccount.getAccountNumber());
    }
}
