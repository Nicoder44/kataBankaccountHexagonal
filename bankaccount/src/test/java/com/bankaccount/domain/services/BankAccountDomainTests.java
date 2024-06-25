package com.bankaccount.domain.services;

import com.bankaccount.application.exceptions.BankAccountError;
import com.bankaccount.application.exceptions.BankAccountException;
import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.LimitedBankAccount;
import com.bankaccount.domain.ports.BankAccountRepository;
import com.bankaccount.infrastructure.adapters.repositories.SpringDataJpaTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BankAccountDomainTests {

    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private SpringDataJpaTransactionRepository transactionRepository;

    @InjectMocks
    private BankAccountDomain bankAccountDomain;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        // Given
        double initialBalance = 100.0;
        double overdraftLimit = 500.0;

        BankAccount expectedAccount = new BankAccount(initialBalance, overdraftLimit);
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(expectedAccount);

        // When
        BankAccount createdAccount = bankAccountDomain.createAccount(initialBalance, overdraftLimit);

        // Then
        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.getBalance()).isEqualTo(initialBalance);
        assertThat(createdAccount.getOverdraftLimit()).isEqualTo(overdraftLimit);

        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
    }

    @Test
    void testCreateLimitedAccount() {
        // Given
        double initialBalance = 100.0;
        double depositLimit = 500.0;

        LimitedBankAccount expectedAccount = new LimitedBankAccount(initialBalance, depositLimit);
        when(bankAccountRepository.save(any(LimitedBankAccount.class))).thenReturn(expectedAccount);

        // When
        LimitedBankAccount createdAccount = bankAccountDomain.createLimitedAccount(initialBalance, depositLimit);

        // Then
        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.getBalance()).isEqualTo(initialBalance);
        assertThat(createdAccount.getOverdraftLimit()).isZero();
        assertThat(createdAccount.getDepositLimit()).isEqualTo(depositLimit);

        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
    }

    @Test
    void testGetAccount_existingAccount() {
        // Given
        UUID accountNumber = UUID.randomUUID();
        BankAccount mockAccount = new BankAccount(100.0, 0);
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(mockAccount);

        // When
        BankAccount retrievedAccount = bankAccountDomain.getAccount(accountNumber);

        // Then
        assertThat(retrievedAccount).isNotNull().isEqualTo(mockAccount);
        assertThat(retrievedAccount.getBalance()).isEqualTo(100.0);
    }

    @Test
    void testGetAccount_nonExistingAccount() {
        // Given
        UUID accountNumber = UUID.randomUUID();
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(null);

        // When / Then
        assertThatThrownBy(() -> bankAccountDomain.getAccount(accountNumber))
                .isInstanceOf(BankAccountException.class)
                .hasFieldOrPropertyWithValue("error", BankAccountError.ACCOUNT_NOT_FOUND);
        verify(bankAccountRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void testDeposit_success() {
        // Given
        UUID accountNumber = UUID.randomUUID();
        BankAccount account = new BankAccount(100.0, 0);
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        when(bankAccountRepository.save(account)).thenReturn(account);
        double depositAmount = 50.0;

        // When
        BankAccount updatedAccount = bankAccountDomain.deposit(accountNumber, depositAmount);

        // Then
        assertEquals(150.0, updatedAccount.getBalance());
        verify(bankAccountRepository, times(1)).save(account);
    }

    @Test
    void testDeposit_accountNotFound() {
        // Given
        UUID accountNumber = UUID.randomUUID();
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        double depositAmount = 50.0;

        // When / Then
        assertThrows(BankAccountException.class, () -> bankAccountDomain.deposit(accountNumber, depositAmount));
        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    void testWithdraw_success() {
        // Given
        UUID accountNumber = UUID.randomUUID();
        BankAccount account = new BankAccount(100.0, 0);
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        when(bankAccountRepository.save(account)).thenReturn(account);
        double withdrawAmount = 50.0;

        // When
        BankAccount updatedAccount = bankAccountDomain.withdraw(accountNumber, withdrawAmount);

        // Then
        assertEquals(50.0, updatedAccount.getBalance());
        verify(bankAccountRepository, times(1)).save(account);
    }

    @Test
    void testWithdraw_insufficientBalance() {
        // Given
        UUID accountNumber = UUID.randomUUID();
        BankAccount account = new BankAccount(100.0, 0);
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        double withdrawAmount = 150.0;

        // When / Then
        assertThrows(BankAccountException.class, () -> bankAccountDomain.withdraw(accountNumber, withdrawAmount));
        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    void testSetOverDraftLimit_success() {
        // Given
        UUID accountNumber = UUID.randomUUID();
        BankAccount account = new BankAccount(100.0, 0);
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        when(bankAccountRepository.save(account)).thenReturn(account);
        double newOverdraftLimit = 200.0;

        // When
        BankAccount updatedAccount = bankAccountDomain.setOverDraftLimit(accountNumber, newOverdraftLimit);

        // Then
        assertEquals(200.0, updatedAccount.getOverdraftLimit());
        verify(bankAccountRepository, times(1)).save(account);
    }

    @Test
    void testSetOverDraftLimit_negativeLimit() {
        // Given
        UUID accountNumber = UUID.randomUUID();
        BankAccount account = new BankAccount(100.0, 0);
        when(bankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(account);
        double newOverdraftLimit = -50.0;

        // When / Then
        assertThrows(BankAccountException.class, () -> bankAccountDomain.setOverDraftLimit(accountNumber, newOverdraftLimit));
        verify(bankAccountRepository, never()).save(any());
    }
}