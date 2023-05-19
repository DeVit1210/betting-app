package com.betting.user.player.account;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PlayerRepository playerRepository;
    public void bind(Player player) {
        Account account = new Account(player);
        accountRepository.save(account);
        player.setAccount(account);
        playerRepository.save(player);
    }

    public Account findAccountByPlayer(Long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new EntityNotFoundException(Player.class));
        return accountRepository.findAccountByPlayer(player)
                .orElseThrow(() -> new EntityNotFoundException(Account.class));
    }

    public BettingResponse findAllAccounts() {
        return BettingResponse.builder().entities(accountRepository.findAll()).build();
    }

    // TODO: simple implementation. without balance checking
    public void replenish(Account account, double moneyAmount) {
        accountRepository.updateAccount(account.getCurrentMoneyAmount() + moneyAmount, account.getId());
    }

    public void withdraw(Account account, double moneyAmount) {
        accountRepository.updateAccount(account.getCurrentMoneyAmount() - moneyAmount, account.getId());
    }
}
