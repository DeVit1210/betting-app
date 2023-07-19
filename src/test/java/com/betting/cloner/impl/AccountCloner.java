package com.betting.cloner.impl;

import com.betting.cloner.TestCloner;
import com.betting.user.player.account.Account;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "anAccountCloner")
public class AccountCloner implements TestCloner<Account> {
    @Override
    public Account clone(Account account) {
        return Account.builder()
                .id(account.getId())
                .player(account.getPlayer())
                .transactions(account.getTransactions())
                .currentMoneyAmount(account.getCurrentMoneyAmount())
                .build();
    }
}
