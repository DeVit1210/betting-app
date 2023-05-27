package com.betting.user.player.account.transaction;

import com.betting.mapping.TransactionRequestMapper;
import com.betting.user.player.account.Account;
import com.betting.user.player.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final TransactionRequestMapper transactionRequestMapper;

    public Transaction addTransaction(Account account, double moneyAmount, boolean moneyOut) {
        Transaction transaction = Transaction.builder()
                .account(account)
                .time(LocalDateTime.now())
                .moneyOut(moneyOut)
                .moneyAmount(moneyAmount)
                .build();
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction addTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = transactionRequestMapper.mapFrom(transactionRequest);
        transactionRepository.save(transaction);
        return transaction;
    }

    public List<Transaction> findTransactionsByAccount(Long accountId) {
        Account account = accountService.findById(accountId);
        return transactionRepository.findTransactionsByAccount(account).toList();
    }
}

