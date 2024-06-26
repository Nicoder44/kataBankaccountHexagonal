package com.bankaccount.applications.controllers;

import com.bankaccount.application.adapters.BankAccountController;
import com.bankaccount.application.services.BankAccountServicePorts;
import com.bankaccount.application.services.StatementServicePorts;
import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.Statement;
import com.bankaccount.domain.models.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BankAccountControllerTests {

    @Mock
    private BankAccountServicePorts bankAccountService;

    @Mock
    private StatementServicePorts statementService;

    @InjectMocks
    private BankAccountController bankAccountController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bankAccountController).build();
    }

    @Test
    void testCreateAccount() throws Exception {
        BankAccount mockAccount = new BankAccount(100.0, 50.0);
        when(bankAccountService.createAccount(anyDouble(), anyDouble())).thenReturn(mockAccount);

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\": 100.0, \"overdraftLimit\": 50.0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.overdraftLimit").value(50.0));

        verify(bankAccountService, times(1)).createAccount(100.0, 50.0);
    }

    @Test
    void testGetAccount() throws Exception {
        UUID accountNumber = UUID.randomUUID();
        BankAccount mockAccount = new BankAccount(150.0, 0);
        when(bankAccountService.getAccount(accountNumber)).thenReturn(mockAccount);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{accountNumber}", accountNumber)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(150.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.overdraftLimit").value(0));

        verify(bankAccountService, times(1)).getAccount(accountNumber);
    }

    @Test
    void testDeposit() throws Exception {
        UUID accountNumber = UUID.randomUUID();
        double depositAmount = 50.0;
        BankAccount mockAccount = new BankAccount(100.0, 0);
        when(bankAccountService.deposit(accountNumber, depositAmount)).thenReturn(mockAccount);

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/{accountNumber}/deposit", accountNumber)
                        .param("amount", "50.0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bankAccountService, times(1)).deposit(accountNumber, depositAmount);
    }

    @Test
    void testWithdraw() throws Exception {
        UUID accountNumber = UUID.randomUUID();
        double withdrawAmount = 30.0;
        BankAccount mockAccount = new BankAccount(100.0, 0);
        when(bankAccountService.withdraw(accountNumber, withdrawAmount)).thenReturn(mockAccount);

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/{accountNumber}/withdraw", accountNumber)
                        .param("amount", "30.0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bankAccountService, times(1)).withdraw(accountNumber, withdrawAmount);
    }

    @Test
    void testSetOverDraftLimit() throws Exception {
        UUID accountNumber = UUID.randomUUID();
        double overdraftLimit = 100.0;
        BankAccount mockAccount = new BankAccount(100.0, 0);
        when(bankAccountService.setOverDraftLimit(accountNumber, overdraftLimit)).thenReturn(mockAccount);

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/{accountNumber}/setOverDraftLimit", accountNumber)
                        .param("amount", "100.0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bankAccountService, times(1)).setOverDraftLimit(accountNumber, overdraftLimit);
    }

    @Test
    void testGetMonthlyStatement() throws Exception {
        UUID accountId = UUID.randomUUID();
        Statement mockStatement = new Statement();
        mockStatement.setTransactions(new ArrayList<Transaction>());

        // Mock the behavior of statementService
        when(statementService.generateMonthlyStatement(accountId)).thenReturn(mockStatement);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{accountId}/statement", accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactions").isArray());

        // Verify that the statementService was called with the correct accountId
        verify(statementService).generateMonthlyStatement(accountId);
    }
}
