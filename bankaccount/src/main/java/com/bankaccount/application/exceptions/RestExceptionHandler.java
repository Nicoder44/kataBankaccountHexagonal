package com.bankaccount.application.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BankAccountException.class)
    public ResponseEntity<ErrorResponse> handleBankAccountException(BankAccountException ex, WebRequest request) {
        log.error("BankAccountException: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getError().getCode(), ex.getError().getMessage(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, WebRequest request) {
        log.error("Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(BankAccountError.GENERIC_ERROR.getCode(), BankAccountError.GENERIC_ERROR.getMessage(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
