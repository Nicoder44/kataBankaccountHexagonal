package com.bankaccount.domain.models;

import com.bankaccount.application.exceptions.BankAccountError;
import com.bankaccount.application.exceptions.BankAccountException;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Entity
@Setter
@NoArgsConstructor
public class LimitedBankAccount extends BankAccount {

    private double depositLimit;

    public LimitedBankAccount(double balance, double depositLimit) {
        super(balance, 0.0);
        if(balance > depositLimit){
            throw new BankAccountException(BankAccountError.DEPOSIT_LIMIT_EXCEEDED, HttpStatus.BAD_REQUEST);
        }
        this.depositLimit = depositLimit;
    }

    @Override
    public void deposit(double amount) {
        if(this.getBalance() + amount > depositLimit){
            throw new BankAccountException(BankAccountError.DEPOSIT_LIMIT_EXCEEDED, HttpStatus.BAD_REQUEST);
        }
        super.deposit(amount);
    }

    @Override
    public void setOverdraftLimit(double overdraftLimit) {
        if(overdraftLimit!=0){
            throw new BankAccountException(BankAccountError.INVALID_OPERATION, HttpStatus.UNAUTHORIZED);
        }
        super.setOverdraftLimit(0);
    }
}
