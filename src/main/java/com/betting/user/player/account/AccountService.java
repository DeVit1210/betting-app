package com.betting.user.player.account;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.util.ThrowableUtils;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerRepository;
import com.betting.user.player.account.transaction.Transaction;
import com.betting.user.player.account.transaction.TransactionRequest;
import com.betting.user.player.account.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PlayerRepository playerRepository;
    private final TransactionService transactionService;

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException(Account.class));
    }
    public void bind(Player player) {
        Account account = new Account(player);
        accountRepository.save(account);
        player.setAccount(account);
        playerRepository.save(player);
    }
    public Account findAccountByPlayer(Long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new EntityNotFoundException(Player.class));
        return accountRepository.findAccountByPlayer(player).orElseThrow(() -> new EntityNotFoundException(Account.class));
    }

    public BettingResponse findAllAccounts() {
        return BettingResponse.builder().entities(accountRepository.findAll()).build();
    }

    public void replenish(Account account, double moneyAmount) {
        transactionService.addTransaction(account, moneyAmount, false);
        accountRepository.updateAccount(account.getCurrentMoneyAmount() + moneyAmount, account.getId());
    }

    public Transaction replenish(TransactionRequest transactionRequest) {
        Transaction transaction = transactionService.addTransaction(transactionRequest);
        Account account = transaction.getAccount();
        accountRepository.updateAccount(account.getCurrentMoneyAmount() + transaction.getMoneyAmount(), account.getId());
        return transaction;
    }

    public void withdraw(Account account, double moneyAmount) {
        ThrowableUtils.trueOrElseThrow(e -> e.getCurrentMoneyAmount() >= moneyAmount, account,
                new IllegalArgumentException("cannot withdraw bigger money amount that the account has"));
        transactionService.addTransaction(account, moneyAmount, true);
        accountRepository.updateAccount(account.getCurrentMoneyAmount() - moneyAmount, account.getId());
    }

    public Transaction withdraw(TransactionRequest request) {
        Transaction transaction = transactionService.addTransaction(request);
        Account account = transaction.getAccount();
        ThrowableUtils.trueOrElseThrow(e -> e.getCurrentMoneyAmount() >= transaction.getMoneyAmount(), account,
                new IllegalArgumentException("cannot withdraw bigger money amount that the account has"));
        accountRepository.updateAccount(account.getCurrentMoneyAmount() - request.getMoneyAmount(), account.getId());
        return transaction;
    }
}
