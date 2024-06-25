package com.bankaccount.applications.services;

import com.bankaccount.application.services.StatementService;
import com.bankaccount.domain.models.Statement;
import com.bankaccount.domain.services.StatementDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StatementServiceTest {

    @Mock
    private StatementDomain statementDomain;

    @InjectMocks
    private StatementService statementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateMonthlyStatement() {
        // Given
        UUID accountId = UUID.randomUUID();
        Statement expectedStatement = new Statement();

        when(statementDomain.generateMonthlyStatement(accountId)).thenReturn(expectedStatement);

        // When
        Statement actualStatement = statementService.generateMonthlyStatement(accountId);

        // Then
        assertEquals(expectedStatement, actualStatement);
        verify(statementDomain).generateMonthlyStatement(accountId);
    }
}
