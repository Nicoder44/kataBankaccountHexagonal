package com.bankaccount.applications.services;

import com.bankaccount.application.services.TransactionService;
import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.Transaction;
import com.bankaccount.domain.services.TransactionDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @Mock
    private TransactionDomain transactionDomain;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransaction() {
        // Given
        BankAccount account = new BankAccount();
        String type = "Deposit";
        double amount = 100.0;
        Transaction expectedTransaction = new Transaction();

        when(transactionDomain.createTransaction(account, type, amount)).thenReturn(expectedTransaction);

        // When
        Transaction actualTransaction = transactionService.createTransaction(account, type, amount);

        // Then
        assertEquals(expectedTransaction, actualTransaction);
        verify(transactionDomain).createTransaction(account, type, amount);
    }

    @Test
    public void testGetTransactionsForAccount() {
        // Given
        UUID accountId = UUID.randomUUID();
        Date startDate = new Date();
        Date endDate = new Date();
        List<Transaction> expectedTransactions = new ArrayList<>(); // Mocked or actual instance

        when(transactionDomain.getTransactionsForAccount(accountId, startDate, endDate)).thenReturn(expectedTransactions);

        // When
        List<Transaction> actualTransactions = transactionService.getTransactionsForAccount(accountId, startDate, endDate);

        // Then
        assertEquals(expectedTransactions, actualTransactions);
        verify(transactionDomain).getTransactionsForAccount(accountId, startDate, endDate);
    }
}
