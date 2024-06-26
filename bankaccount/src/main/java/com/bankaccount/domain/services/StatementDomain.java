package com.bankaccount.domain.services;

import com.bankaccount.application.services.StatementServicePorts;
import com.bankaccount.domain.models.BankAccount;
import com.bankaccount.domain.models.LimitedBankAccount;
import com.bankaccount.domain.models.Statement;
import com.bankaccount.domain.models.Transaction;
import com.bankaccount.domain.ports.out.BankAccountRepository;
import com.bankaccount.domain.ports.out.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StatementDomain implements StatementServicePorts {

    private final BankAccountRepository bankAccountRepository;

    private final TransactionRepository transactionRepository;

    @Override
    public Statement generateMonthlyStatement(UUID accountId) {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountId);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date startDate = calendar.getTime();
        Date endDate = new Date();

        List<Transaction> transactions = transactionRepository.findByAccount_AccountNumberAndDateBetweenOrderByDateDesc(accountId, startDate, endDate);

        Statement statement = new Statement();
        statement.setAccountType(account instanceof LimitedBankAccount ? "Livret" : "Compte Courant");
        statement.setBalance(account.getBalance());
        statement.setGeneratedDate(endDate);
        statement.setNumberOfTransactionsThisMonth(transactions.size());
        statement.setTransactions(transactions);

        return statement;
    }
}
