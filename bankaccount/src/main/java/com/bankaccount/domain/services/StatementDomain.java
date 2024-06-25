package com.bankaccount.domain.services;

import com.bankaccount.application.services.BankAccountService;
import com.bankaccount.application.services.TransactionService;
import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.LimitedBankAccount;
import com.bankaccount.domain.models.Statement;
import com.bankaccount.domain.models.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StatementDomain {

    private final BankAccountService bankAccountService;

    private final TransactionService transactionService;

    public Statement generateMonthlyStatement(UUID accountId) {
        BankAccount account = bankAccountService.getAccount(accountId);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date startDate = calendar.getTime();
        Date endDate = new Date();

        List<Transaction> transactions = transactionService.getTransactionsForAccount(accountId, startDate, endDate);

        Statement statement = new Statement();
        statement.setAccountType(account instanceof LimitedBankAccount ? "Livret" : "Compte Courant");
        statement.setBalance(account.getBalance());
        statement.setGeneratedDate(endDate);
        statement.setNumberOfTransactionsThisMonth(transactions.size());
        statement.setTransactions(transactions);

        return statement;
    }
}
