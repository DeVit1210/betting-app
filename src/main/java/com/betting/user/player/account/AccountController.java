package com.betting.user.player.account;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.user.player.account.transaction.Transaction;
import com.betting.user.player.account.transaction.TransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/replenish")
    public ResponseEntity<Transaction> replenish(@RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity.ok(accountService.replenish(transactionRequest));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity.ok(accountService.withdraw(transactionRequest));
    }
}
