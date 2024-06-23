package com.bankaccount.domain.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BankAccountTest {

    @Test
    public void testDeposit() {
        // Given
        BankAccount account = new BankAccount(100.0, 50.0);
        double initialBalance = account.getBalance();
        double amountToDeposit = 50.0;

        // When
        account.deposit(amountToDeposit);

        // Then
        assertThat(account.getBalance()).isEqualTo(initialBalance + amountToDeposit);
    }

    @Test
    public void testWithdrawSufficientFunds() {
        // Given
        BankAccount account = new BankAccount(100.0, 50.0);
        double initialBalance = account.getBalance();
        double amountToWithdraw = 50.0;

        // When
        boolean successfulWithdraw = account.withdraw(amountToWithdraw);

        // Then
        assertThat(successfulWithdraw).isTrue();
        assertThat(account.getBalance()).isEqualTo(initialBalance - amountToWithdraw);
    }

    @Test
    public void testWithdrawNegativeAmount() {
        // Given
        BankAccount account = new BankAccount(100.0, 50.0);
        double initialBalance = account.getBalance();
        double amountToWithdraw = -5.0;

        // When
        boolean successfulWithdraw = account.withdraw(amountToWithdraw);

        // Then
        assertThat(successfulWithdraw).isFalse();
        assertThat(account.getBalance()).isEqualTo(initialBalance);
    }

    @Test
    public void DeposittestWithdrawNegativeAmount() {
        // Given
        BankAccount account = new BankAccount(100.0, 50.0);
        double initialBalance = account.getBalance();
        double amountToDeposit = -5.0;

        // When
        account.deposit(amountToDeposit);

        // Then
        assertThat(account.getBalance()).isEqualTo(initialBalance);
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        // Given
        BankAccount account = new BankAccount(100.0, 50.0);
        double initialBalance = account.getBalance();
        double amountToWithdraw = 200.0;

        // When
        boolean successfulWithdraw = account.withdraw(amountToWithdraw);

        // Then
        assertThat(successfulWithdraw).isFalse();
        assertThat(account.getBalance()).isEqualTo(initialBalance);
    }

    @Test
    public void testWithdrawOverDraftFunds() {
        // Given
        BankAccount account = new BankAccount(100.0, 50.0);
        double initialBalance = account.getBalance();
        double amountToWithdraw = 130.0;

        // When
        boolean successfulWithdraw = account.withdraw(amountToWithdraw);

        // Then
        assertThat(successfulWithdraw).isTrue();
        assertThat(account.getBalance()).isEqualTo(initialBalance - amountToWithdraw);
    }

    @Test
    public void testSetOverdraftLimit() {
        // Given
        BankAccount account = new BankAccount(100.0, 50.0);
        double newOverDraftLimit = 300.0;

        // When
        account.setOverdraftLimit(newOverDraftLimit);

        // Then
        assertThat(account.getOverdraftLimit()).isEqualTo(newOverDraftLimit);
    }
}
