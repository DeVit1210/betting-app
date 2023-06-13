package com.betting.test_builder.impl;

import com.betting.test_builder.TestBuilder;
import com.betting.user.player.account.Account;
import com.betting.user.player.account.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;

@NoArgsConstructor(staticName = "aTransactionBuilder")
@AllArgsConstructor
@With
public class TransactionBuilder implements TestBuilder<Transaction> {
    private double moneyAmount = 0.0;
    private LocalDateTime time = LocalDateTime.now();
    private Account account = new Account();
    private boolean moneyOut = false;

    @Override
    public Transaction build() {
        return Transaction.builder()
                .moneyAmount(moneyAmount).time(time)
                .moneyOut(moneyOut).account(account)
                .build();
    }
}
