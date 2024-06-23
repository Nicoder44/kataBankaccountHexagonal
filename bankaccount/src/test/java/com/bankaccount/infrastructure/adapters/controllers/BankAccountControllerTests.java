package com.bankaccount.infrastructure.adapters.controllers;

import com.bankaccount.application.services.BankAccountService;
import com.bankaccount.domain.models.BankAccount;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

public class BankAccountControllerTests {

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private BankAccountController bankAccountController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bankAccountController).build();
    }

    @Test
    public void testCreateAccount() throws Exception {
        BankAccount mockAccount = new BankAccount(100.0, 50.0);
        when(bankAccountService.createAccount(anyDouble(), anyDouble())).thenReturn(mockAccount);

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\": 100.0, \"overdraftLimit\": 50.0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.overdraftLimit").value(50.0));

        verify(bankAccountService, times(1)).createAccount(100.0, 50.0);
    }

    @Test
    public void testGetAccount() throws Exception {
        UUID accountNumber = UUID.randomUUID();
        BankAccount mockAccount = new BankAccount(150.0, 0);
        when(bankAccountService.getAccount(accountNumber)).thenReturn(mockAccount);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{accountNumber}", accountNumber)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(150.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.overdraftLimit").value(0));

        verify(bankAccountService, times(1)).getAccount(accountNumber);
    }

    @Test
    public void testDeposit() throws Exception {
        UUID accountNumber = UUID.randomUUID();
        double depositAmount = 50.0;
        BankAccount mockAccount = new BankAccount(100.0, 0);
        when(bankAccountService.deposit(accountNumber, depositAmount)).thenReturn(mockAccount);

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/{accountNumber}/deposit", accountNumber)
                        .param("amount", "50.0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(bankAccountService, times(1)).deposit(accountNumber, depositAmount);
    }

    @Test
    public void testWithdraw() throws Exception {
        UUID accountNumber = UUID.randomUUID();
        double withdrawAmount = 30.0;
        BankAccount mockAccount = new BankAccount(100.0, 0);
        when(bankAccountService.withdraw(accountNumber, withdrawAmount)).thenReturn(mockAccount);

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/{accountNumber}/withdraw", accountNumber)
                        .param("amount", "30.0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(bankAccountService, times(1)).withdraw(accountNumber, withdrawAmount);
    }

    @Test
    public void testSetOverDraftLimit() throws Exception {
        UUID accountNumber = UUID.randomUUID();
        double overdraftLimit = 100.0;
        BankAccount mockAccount = new BankAccount(100.0, 0);
        when(bankAccountService.setOverdraftLimit(accountNumber, overdraftLimit)).thenReturn(mockAccount);

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/{accountNumber}/setOverDraftLimit", accountNumber)
                        .param("amount", "100.0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(bankAccountService, times(1)).setOverdraftLimit(accountNumber, overdraftLimit);
    }
}
