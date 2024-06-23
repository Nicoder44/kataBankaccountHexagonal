package com.bankaccount.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BankAccountError {

    ACCOUNT_NOT_FOUND("AOO1","Account not found"),
    INSUFFICIENT_BALANCE("A002", "Insufficient balance"),
    GENERIC_ERROR("A003", "An unexpected error occurred"),
    INVALID_VALUE("A004", "The amount should be positive"),
    DEPOSIT_LIMIT_EXCEEDED("A005", "The maximum balance on this account does not allow this amount for deposit"),
    INVALID_OPERATION("A006", "The rights on this account does not match your request");

    private final String code;
    private final String message;
}
