package com.bankaccount.applications.exceptions;

import com.bankaccount.application.exceptions.BankAccountError;
import com.bankaccount.application.exceptions.BankAccountException;
import com.bankaccount.application.exceptions.ErrorResponse;
import com.bankaccount.application.exceptions.RestExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    public void testHandleBankAccountException() {
        // Given
        BankAccountException ex = new BankAccountException(BankAccountError.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND);

        // When
        ResponseEntity<ErrorResponse> responseEntity = handler.handleBankAccountException(ex, null);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getErrorCode()).isEqualTo(BankAccountError.ACCOUNT_NOT_FOUND.getCode());
        assertThat(responseEntity.getBody().getErrorMessage()).isEqualTo(BankAccountError.ACCOUNT_NOT_FOUND.getMessage());
        assertThat(responseEntity.getBody().getDetails()).isEqualTo(BankAccountError.ACCOUNT_NOT_FOUND.getMessage());
    }

    @Test
    public void testHandleGeneralException() {
        // Given
        Exception ex = new Exception("test_message");

        // When
        ResponseEntity<ErrorResponse> responseEntity = handler.handleGeneralException(ex, null);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getErrorCode()).isEqualTo(BankAccountError.GENERIC_ERROR.getCode());
        assertThat(responseEntity.getBody().getErrorMessage()).isEqualTo(BankAccountError.GENERIC_ERROR.getMessage());
        assertThat(responseEntity.getBody().getDetails()).isEqualTo("test_message");
    }
}
