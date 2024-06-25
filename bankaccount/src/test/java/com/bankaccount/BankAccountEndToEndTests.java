package com.bankaccount;

import com.bankaccount.application.services.StatementService;
import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.LimitedBankAccount;
import com.bankaccount.domain.models.Statement;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BankAccountEndToEndTests {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private StatementService statementService;

    private UUID accountNumber;

    @BeforeEach
    public void setup() {
        BankAccount account = createAccount(100.0, 0.0);
        this.accountNumber = account.getAccountNumber();
    }

    @Test
    void testCreateAccount() {
        // Given
        double initialBalance = 200.0;
        double overdraftLimit = 50.0;

        // When
        BankAccount createdAccount = createAccount(initialBalance, overdraftLimit);

        // Then
        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.getAccountNumber()).isNotNull();
        assertThat(createdAccount.getBalance()).isEqualTo(initialBalance);
        assertThat(createdAccount.getOverdraftLimit()).isEqualTo(overdraftLimit);
    }

    @Test
    void testCreateLimitedAccount() {
        // Given
        double initialBalance = 200.0;
        double depositLimit = 250.0;

        // When
        LimitedBankAccount createdAccount = createLimitedAccount(initialBalance, depositLimit);

        // Then
        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.getAccountNumber()).isNotNull();
        assertThat(createdAccount.getBalance()).isEqualTo(initialBalance);
        assertThat(createdAccount.getDepositLimit()).isEqualTo(depositLimit);
    }

    @Test
    void testDepositThenStatement() {
        // Given
        double depositAmount = 50.0;

        // When
        BankAccount updatedAccount = deposit(accountNumber, depositAmount);

        // Then
        assertThat(updatedAccount).isNotNull();
        assertThat(updatedAccount.getBalance()).isEqualTo(150.0);

        // Verify transaction added
        assertThat(generateMonthlyStatement(updatedAccount.getAccountNumber()).getTransactions()).hasSize(1);
        assertThat(generateMonthlyStatement(updatedAccount.getAccountNumber()).getTransactions().getFirst().getType()).isEqualTo("DEPOSIT");
        assertThat(generateMonthlyStatement(updatedAccount.getAccountNumber()).getTransactions().getFirst().getAmount()).isEqualTo(depositAmount);
    }

    @Test
    void testWithdrawOnLimitedAccountThenDepositFailThenStatement() {

        double withdrawalAmount = 50.0;
        BankAccount account = createLimitedAccount(50, 200);

        BankAccount updatedAccount = withdraw(account.getAccountNumber(), withdrawalAmount);
        assertThat(updatedAccount.getBalance()).isZero();

        depositFailExpected(account.getAccountNumber(), 1000);

        assertThat(updatedAccount.getBalance()).isZero(); // Should be the same because deposit limit is too low

        // Verify transaction added
        assertThat(generateMonthlyStatement(updatedAccount.getAccountNumber()).getTransactions()).hasSize(1);
        assertThat(generateMonthlyStatement(updatedAccount.getAccountNumber()).getTransactions().getFirst().getType()).isEqualTo("WITHDRAWAL");
        assertThat(generateMonthlyStatement(updatedAccount.getAccountNumber()).getTransactions().getFirst().getAmount()).isEqualTo(50);
    }

    @Test
    void testNegativeDeposit() {
        // Given
        double depositAmount = -50.0;

        // When
        deposit(accountNumber, depositAmount);

        // Then
        BankAccount account = getAccount(accountNumber);
        assertThat(account.getBalance()).isEqualTo(100.0);
    }

    @Test
    void testWithdraw() {
        // Given
        double withdrawAmount = 30.0;

        // When
        BankAccount updatedAccount = withdraw(accountNumber, withdrawAmount);

        // Then
        assertThat(updatedAccount).isNotNull();
        assertThat(updatedAccount.getBalance()).isEqualTo(70.0); // 100.0 initial - 30.0 withdrawal
    }

    @Test
    void testSetOverdraftLimit() {
        // Given
        double newOverdraftLimit = 100.0;

        // When
        BankAccount updatedAccount = setOverdraftLimit(accountNumber, newOverdraftLimit);

        // Then
        assertThat(updatedAccount).isNotNull();
        assertThat(updatedAccount.getOverdraftLimit()).isEqualTo(newOverdraftLimit);
    }

    private BankAccount createAccount(double initialBalance, double overdraftLimit) {
        String jsonBody = "{\"balance\": " + initialBalance + ", \"overdraftLimit\": " + overdraftLimit + "}";

        return webTestClient.post()
                .uri("/accounts/createAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankAccount.class)
                .returnResult().getResponseBody();
    }

    private LimitedBankAccount createLimitedAccount(double initialBalance, double depositLimit) {
        String jsonBody = "{\"balance\": " + initialBalance + ", \"depositLimit\": " + depositLimit + "}";

        return webTestClient.post()
                .uri("/limitedAccounts/createAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LimitedBankAccount.class)  // Assurez-vous que la désérialisation fonctionne correctement ici
                .returnResult().getResponseBody();
    }

    private BankAccount getAccount(UUID accountNumber) {
        return webTestClient.get()
                .uri("/accounts/{accountNumber}", accountNumber)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankAccount.class)
                .returnResult().getResponseBody();
    }

    private BankAccount deposit(UUID accountNumber, double amount) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("amount", String.valueOf(amount));

        return webTestClient.post()
                .uri("/accounts/{accountNumber}/deposit", accountNumber)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankAccount.class)
                .returnResult().getResponseBody();
    }

    private void depositFailExpected(UUID accountNumber, double amount) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("amount", String.valueOf(amount));

        webTestClient.post()
                .uri("/accounts/{accountNumber}/deposit", accountNumber)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .exchange()
                .expectStatus().isBadRequest();
    }

    private BankAccount withdraw(UUID accountNumber, double amount) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("amount", String.valueOf(amount));

        return webTestClient.post()
                .uri("/accounts/{accountNumber}/withdraw", accountNumber)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankAccount.class)
                .returnResult().getResponseBody();
    }

    private BankAccount setOverdraftLimit(UUID accountNumber, double amount) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("amount", String.valueOf(amount));

        return webTestClient.post()
                .uri("/accounts/{accountNumber}/setOverDraftLimit", accountNumber)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankAccount.class)
                .returnResult().getResponseBody();
    }

    private Statement generateMonthlyStatement(UUID accountNumber) {
        return webTestClient.get()
                .uri("accounts/{accountNumber}/statement", accountNumber)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Statement.class)
                .returnResult().getResponseBody();
    }

}
