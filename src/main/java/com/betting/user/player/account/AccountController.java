package com.betting.user.player.account;

import com.betting.events.betting_entity.BettingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/find/{playerId}")
    public ResponseEntity<Account> findAccountByPlayer(@PathVariable Long playerId) {
        return ResponseEntity.ok(accountService.findAccountByPlayer(playerId));
    }

    @GetMapping("/")
    public ResponseEntity<BettingResponse> findAllAccounts() {
        return ResponseEntity.ok(accountService.findAllAccounts());
    }
}
