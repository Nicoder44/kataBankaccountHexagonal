package com.bankaccount.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BankAccountError {

    ACCOUNT_NOT_FOUND("AOO1","Account not found"),
    INSUFFICIENT_BALANCE("A002", "Insufficient balance"),
    GENERIC_ERROR("A003", "An unexpected error occurred"),
    INVALID_VALUE("A004", "The amount should be positive");

    private final String code;
    private final String message;
}
