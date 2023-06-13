package com.betting.test_builder.impl;

import com.betting.test_builder.TestBuilder;
import com.betting.user.player.Player;
import com.betting.user.player.account.Account;
import com.betting.user.player.account.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(staticName = "anAccountBuilder")
@AllArgsConstructor
@With
public class AccountBuilder implements TestBuilder<Account> {
    private double currentMoneyAmount = 0.0;
    private Player player = null;
    private List<Transaction> transactions = Collections.emptyList();

    @Override
    public Account build() {
        Account account = new Account();
        account.setPlayer(player);
        account.setTransactions(transactions);
        account.setCurrentMoneyAmount(currentMoneyAmount);
        return account;
    }
}
