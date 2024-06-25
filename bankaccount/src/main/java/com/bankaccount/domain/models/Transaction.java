package com.bankaccount.domain.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccount account;

    private Date date;

    private String type;

    private double balanceAfterTransaction;

    private double amount;

    public Transaction(BankAccount account, Date date, String type, double amount) {
        this.account = account;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.balanceAfterTransaction = account.getBalance();
    }
}