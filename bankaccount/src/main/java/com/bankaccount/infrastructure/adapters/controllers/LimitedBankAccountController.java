package com.bankaccount.infrastructure.adapters.controllers;

import com.bankaccount.application.services.LimitedBankAccountService;
import com.bankaccount.domain.models.LimitedBankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/limitedAccounts")
public class LimitedBankAccountController {

    @Autowired
    private LimitedBankAccountService limitedBankAccountService;

    @PostMapping(value = "/createAccount", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public LimitedBankAccount createAccount(@RequestBody LimitedBankAccount account) {
        return limitedBankAccountService.createAccount(account.getBalance(), account.getDepositLimit());
    }
}
