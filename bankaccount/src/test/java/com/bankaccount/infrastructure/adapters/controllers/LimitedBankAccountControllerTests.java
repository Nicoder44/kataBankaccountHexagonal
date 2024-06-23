package com.bankaccount.infrastructure.adapters.controllers;

import com.bankaccount.application.services.LimitedBankAccountService;
import com.bankaccount.domain.models.LimitedBankAccount;
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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class LimitedBankAccountControllerTests {

    @Mock
    private LimitedBankAccountService limitedBankAccountService;

    @InjectMocks
    private LimitedBankAccountController limitedBankAccountController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(limitedBankAccountController).build();
    }

    @Test
    public void testCreateLimitedAccount() throws Exception {
        // Given
        Map<String, Double> payload = new HashMap<>();
        payload.put("balance", 100.0);
        payload.put("depositLimit", 200.0);

        LimitedBankAccount mockAccount = new LimitedBankAccount(100.0, 200.0);
        when(limitedBankAccountService.createAccount(100.0, 200.0)).thenReturn(mockAccount);

        // When / Then
        mockMvc.perform(MockMvcRequestBuilders.post("/limitedAccounts/createAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\": 100.0, \"depositLimit\": 200.0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.depositLimit").value(200.0));

        verify(limitedBankAccountService, times(1)).createAccount(100.0, 200.0);
    }
}