package com.bankaccount.domain.services;

import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.Transaction;
import com.bankaccount.domain.ports.out.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionDomainTest {

    @Mock
    private TransactionRepository mockTransactionRepository;

    @InjectMocks
    private TransactionDomain transactionDomain;

    private BankAccount mockAccount;
    private UUID accountId;
    private Date currentDate;

    @BeforeEach
    public void setup() {
        mockAccount = new BankAccount();
        accountId = UUID.randomUUID();
        currentDate = new Date();
    }

    @Test
    void testCreateTransaction() {
        // Given
        String transactionType = "DEPOSIT";
        double amount = 100.0;
        Transaction transaction = new Transaction(mockAccount, currentDate, transactionType, amount);

        doAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(UUID.randomUUID());
            return savedTransaction;
        }).when(mockTransactionRepository).save(any(Transaction.class));

        // When
        Transaction createdTransaction = transactionDomain.createTransaction(mockAccount, transactionType, amount);

        // Then
        assertNotNull(createdTransaction.getId()); // Assuming ID is assigned during save
        assertEquals(transaction.getType(), createdTransaction.getType());
        assertEquals(transaction.getAmount(), createdTransaction.getAmount());

        verify(mockTransactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testGetTransactionsForAccount() {
        // Given
        Date startDate = new Date();
        Date endDate = new Date();
        List<Transaction> expectedTransactions = Arrays.asList(
                new Transaction(mockAccount, new Date(), "DEPOSIT", 100.0),
                new Transaction(mockAccount, new Date(), "WITHDRAWAL", 50.0)
        );

        when(mockTransactionRepository.findByAccount_AccountNumberAndDateBetweenOrderByDateDesc(accountId, startDate, endDate))
                .thenReturn(expectedTransactions);

        // When
        List<Transaction> retrievedTransactions = transactionDomain.getTransactionsForAccount(accountId, startDate, endDate);

        // Then
        assertEquals(expectedTransactions.size(), retrievedTransactions.size());
        assertEquals(expectedTransactions.get(0), retrievedTransactions.get(0));
        assertEquals(expectedTransactions.get(1), retrievedTransactions.get(1));
        verify(mockTransactionRepository, times(1)).findByAccount_AccountNumberAndDateBetweenOrderByDateDesc(accountId, startDate, endDate);
    }
}
