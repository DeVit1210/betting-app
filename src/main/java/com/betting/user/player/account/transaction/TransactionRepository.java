package com.betting.user.player.account.transaction;

import com.betting.user.player.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Streamable<Transaction> findTransactionsByAccount(Account account);
}
