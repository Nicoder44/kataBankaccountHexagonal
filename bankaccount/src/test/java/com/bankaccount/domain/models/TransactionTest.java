package com.bankaccount.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionTest {

    @Mock
    private BankAccount mockAccount;

    private Date currentDate;

    @BeforeEach
    public void setup() {
        currentDate = new Date();
    }

    @Test
    public void testTransactionInitialization() {
        // Given
        double amount = 100.0;
        when(mockAccount.getBalance()).thenReturn(500.0);

        // When
        Transaction transaction = new Transaction(mockAccount, currentDate, "DEPOSIT", amount);

        // Then
        assertEquals(mockAccount, transaction.getAccount());
        assertEquals(currentDate, transaction.getDate());
        assertEquals("DEPOSIT", transaction.getType());
        assertEquals(amount, transaction.getAmount());
        assertEquals(500.0, transaction.getBalanceAfterTransaction());
    }

    @Test
    public void testTransactionInitializationWithWithdrawal() {
        // Given
        double amount = 50.0;
        when(mockAccount.getBalance()).thenReturn(150.0);

        // When
        Transaction transaction = new Transaction(mockAccount, currentDate, "WITHDRAWAL", amount);

        // Then
        assertEquals(mockAccount, transaction.getAccount());
        assertEquals(currentDate, transaction.getDate());
        assertEquals("WITHDRAWAL", transaction.getType());
        assertEquals(amount, transaction.getAmount());
        assertEquals(150.0, transaction.getBalanceAfterTransaction());
    }
}
