package com.betting.mapping;

import com.betting.user.player.account.Account;
import com.betting.user.player.account.AccountRepository;
import com.betting.user.player.account.transaction.Transaction;
import com.betting.user.player.account.transaction.TransactionRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@Scope("prototype")
public class TransactionRequestMapper implements ObjectMapper<TransactionRequest, Transaction> {
    private AccountRepository accountRepository;

    @Override
    public Transaction mapFrom(TransactionRequest request) {
        Account account = accountRepository.findById(request.accountId()).orElseThrow();
        return Transaction.builder()
                .account(account)
                .time(LocalDateTime.now())
                .moneyAmount(request.moneyAmount())
                .moneyOut(request.out())
                .build();
    }
}
