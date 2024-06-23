package com.bankaccount.applications.services;

import com.bankaccount.application.services.LimitedBankAccountService;
import com.bankaccount.domain.models.LimitedBankAccount;
import com.bankaccount.domain.services.BankAccountDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class LimitedBankAccountServiceTest {

    @Mock
    private BankAccountDomain bankAccountDomain;

    @InjectMocks
    private LimitedBankAccountService bankAccountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLimitedAccount() {
        // Given
        double initialBalance = 25.0;
        double depositLimit = 50.0;
        LimitedBankAccount mockAccount = new LimitedBankAccount(initialBalance, depositLimit);
        when(bankAccountDomain.createLimitedAccount(eq(initialBalance), eq(depositLimit))).thenReturn(mockAccount);

        // When
        LimitedBankAccount createdAccount = bankAccountService.createAccount(initialBalance, depositLimit);

        // Then
        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.getBalance()).isEqualTo(initialBalance);
        assertThat(createdAccount.getDepositLimit()).isEqualTo(depositLimit);
    }
}
