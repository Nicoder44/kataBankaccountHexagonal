package com.bankaccount.applications.services;


import com.bankaccount.application.services.BankAccountService;
import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.services.BankAccountDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


public class BankAccountServiceTest {

    @Mock
    private BankAccountDomain bankAccountDomain;

    @InjectMocks
    private BankAccountService bankAccountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAccount() {
        // Given
        double initialBalance = 100.0;
        double overdraftLimit = 50.0;
        BankAccount mockAccount = new BankAccount(initialBalance, overdraftLimit);
        when(bankAccountDomain.createAccount(eq(initialBalance), eq(overdraftLimit))).thenReturn(mockAccount);

        // When
        BankAccount createdAccount = bankAccountService.createAccount(initialBalance, overdraftLimit);

        // Then
        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.getBalance()).isEqualTo(initialBalance);
        assertThat(createdAccount.getOverdraftLimit()).isEqualTo(overdraftLimit);
    }

    @Test
    public void testGetAccount() {
        // Given
        UUID accountNumber = UUID.randomUUID();
        BankAccount mockAccount = new BankAccount(100.0, 50.0);
        when(bankAccountDomain.getAccount(eq(accountNumber))).thenReturn(mockAccount);

        // When
        BankAccount retrievedAccount = bankAccountService.getAccount(accountNumber);

        // Then
        assertThat(retrievedAccount).isNotNull();
        assertThat(retrievedAccount.getBalance()).isEqualTo(100.0);
        assertThat(retrievedAccount.getOverdraftLimit()).isEqualTo(50.0);
    }
}
