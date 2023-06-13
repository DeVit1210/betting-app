package com.betting.user.player.account;

import com.betting.test_builder.impl.AccountBuilder;
import com.betting.test_builder.impl.PlayerBuilder;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PlayerRepository playerRepository;
    private Player player;

    @BeforeEach
    void setUp() {
        player = PlayerBuilder.aPlayerBuilder().build();
        playerRepository.save(player);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Test
    void testUpdateAccountSuccess() {
        final double initialMoneyAmount = 10.00;
        final double resultingMoneyAmount = 20.00;
        Account account = accountRepository.save(
                AccountBuilder.anAccountBuilder()
                        .withCurrentMoneyAmount(initialMoneyAmount)
                        .withPlayer(player)
                        .build()
        );
        accountRepository.updateAccount(resultingMoneyAmount, account.getId());
        assertEquals(resultingMoneyAmount, accountRepository.findById(account.getId()).orElseThrow().getCurrentMoneyAmount());
    }

    @Test
    void testFindAccountByPlayerSuccess() {
        Account accountToFind = accountRepository.save(AccountBuilder.anAccountBuilder().withPlayer(player).build());
        Account accountToIgnore = accountRepository.save(AccountBuilder.anAccountBuilder().build());
        accountRepository.saveAll(List.of(accountToFind, accountToIgnore));
        Optional<Account> result = accountRepository.findAccountByPlayer(player);
        assertTrue(result.isPresent());
        assertEquals(accountToFind.getId(), result.orElseThrow().getId());
    }

    @Test
    void testFindAccountByPlayerNotFound() {
        Account accountToIgnore = accountRepository.save(AccountBuilder.anAccountBuilder().build());
        accountRepository.save(accountToIgnore);
        Optional<Account> result = accountRepository.findAccountByPlayer(player);
        assertTrue(result.isEmpty());
    }

}