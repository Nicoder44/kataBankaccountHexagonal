package com.bankaccount.domain.models;

import com.bankaccount.application.exceptions.BankAccountError;
import com.bankaccount.application.exceptions.BankAccountException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

public class LimitedBankAccountTest {

    @Test
    public void testCreateAccountWithExceedingInitialBalance() {
        // Given
        double initialBalance = 100.0;
        double depositLimit = 50.0;

        // When / Then
        assertThatExceptionOfType(BankAccountException.class)
                .isThrownBy(() -> {
                    new LimitedBankAccount(initialBalance, depositLimit);
                }).extracting(BankAccountException::getError, BankAccountException::getHttpStatus)
                .containsExactly(BankAccountError.DEPOSIT_LIMIT_EXCEEDED, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDepositWithinLimit() {
        // Given
        LimitedBankAccount account = new LimitedBankAccount(0.0, 100.0);
        double amountToDeposit = 50.0;

        // When
        account.deposit(amountToDeposit);

        // Then
        assertThat(account.getBalance()).isEqualTo(amountToDeposit);
    }

    @Test
    public void testDepositExceedingLimit() {
        // Given
        LimitedBankAccount account = new LimitedBankAccount(50.0, 100.0);
        double amountToDeposit = 60.0;

        // When / Then
        assertThatExceptionOfType(BankAccountException.class)
                .isThrownBy(() -> {
                    account.deposit(amountToDeposit);
                }).extracting(BankAccountException::getError, BankAccountException::getHttpStatus)
                .containsExactly(BankAccountError.DEPOSIT_LIMIT_EXCEEDED, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWithdrawWithinBalance() {
        // Given
        LimitedBankAccount account = new LimitedBankAccount(100.0, 200.0);
        double amountToWithdraw = 50.0;
        double initialBalance = account.getBalance();

        // When
        boolean successfulWithdraw = account.withdraw(amountToWithdraw);

        // Then
        assertThat(successfulWithdraw).isTrue();
        assertThat(account.getBalance()).isEqualTo(initialBalance - amountToWithdraw);
    }

    @Test
    public void testWithdrawExceedingBalance() {
        // Given
        LimitedBankAccount account = new LimitedBankAccount(100.0, 200.0);
        double amountToWithdraw = 150.0;
        double initialBalance = account.getBalance();

        // When
        boolean successfulWithdraw = account.withdraw(amountToWithdraw);

        // Then
        assertThat(successfulWithdraw).isFalse();
        assertThat(account.getBalance()).isEqualTo(initialBalance);
    }

    @Test
    public void testSetOverdraftLimit() {
        // Given
        LimitedBankAccount account = new LimitedBankAccount(100.0, 200.0);

        // When / Then
        assertThatExceptionOfType(BankAccountException.class)
                .isThrownBy(() -> {
                    account.setOverdraftLimit(50.0);
                }).extracting(BankAccountException::getError, BankAccountException::getHttpStatus)
                .containsExactly(BankAccountError.INVALID_OPERATION, HttpStatus.UNAUTHORIZED);
    }
}
