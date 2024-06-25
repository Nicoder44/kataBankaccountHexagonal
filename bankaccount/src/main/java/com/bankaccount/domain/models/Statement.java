package com.bankaccount.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Statement {
    private String accountType;
    private double balance;
    private List<Transaction> transactions;
    private Date generatedDate;
    private Integer numberOfTransactionsThisMonth;
}