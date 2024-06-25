package com.bankaccount.infrastructure.adapters.repositories;

import com.bankaccount.domain.models.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class JpaBankAccountRepositoryTests {

    @Mock
    private SpringDataJpaBankAccountRepository springDataJpaBankAccountRepository;

    @InjectMocks
    private JpaBankAccountRepository jpaBankAccountRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        // Given
        BankAccount mockAccount = new BankAccount(100.0, 50.0);
        when(springDataJpaBankAccountRepository.save(any(BankAccount.class))).thenReturn(mockAccount);

        // When
        BankAccount savedAccount = jpaBankAccountRepository.save(mockAccount);

        // Then
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getBalance()).isEqualTo(100.0);
        assertThat(savedAccount.getOverdraftLimit()).isEqualTo(50.0);
    }

    @Test
    void testFindByAccountNumber_found() {
        // Given
        UUID accountNumber = UUID.randomUUID();
        BankAccount mockAccount = new BankAccount(150.0, 0);
        when(springDataJpaBankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(mockAccount));

        // When
        BankAccount foundAccount = jpaBankAccountRepository.findByAccountNumber(accountNumber);

        // Then
        assertThat(foundAccount).isNotNull();
        assertThat(foundAccount.getBalance()).isEqualTo(150.0);
        assertThat(foundAccount.getOverdraftLimit()).isZero();
    }

    @Test
    void testFindByAccountNumber_notFound() {
        // Given
        UUID accountNumber = UUID.randomUUID();
        when(springDataJpaBankAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        // When
        BankAccount foundAccount = jpaBankAccountRepository.findByAccountNumber(accountNumber);

        // Then
        assertThat(foundAccount).isNull();
    }
}
