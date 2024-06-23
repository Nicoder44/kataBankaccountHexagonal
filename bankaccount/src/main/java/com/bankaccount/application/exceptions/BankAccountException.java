package com.bankaccount.application.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BankAccountException extends RuntimeException {

    private final BankAccountError error;
    private final HttpStatus httpStatus;

    public BankAccountException(BankAccountError error, HttpStatus httpStatus) {
        super(error.getMessage());
        this.error = error;
        this.httpStatus = httpStatus;
    }

}