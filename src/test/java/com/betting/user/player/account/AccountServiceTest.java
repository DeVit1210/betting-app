package com.betting.user.player.account;

import com.betting.exceptions.EntityNotFoundException;
import com.betting.test_builder.impl.AccountBuilder;
import com.betting.test_builder.impl.PlayerBuilder;
import com.betting.test_builder.impl.TransactionBuilder;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerRepository;
import com.betting.user.player.account.transaction.Transaction;
import com.betting.user.player.account.transaction.TransactionRequest;
import com.betting.user.player.account.transaction.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionService transactionService;
    @Mock
    private PlayerRepository playerRepository;
    @Value("${test.exception.account-not-found}")
    private String accountNotFoundMessage;

    @Test
    void testFindByIdSuccess() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(AccountBuilder.anAccountBuilder().build()));
        assertDoesNotThrow(() -> accountService.findById(1L));
    }

    @Test
    void testFindByIdNotFound() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.findById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(accountNotFoundMessage);
    }

    @Test
    void testFindAccountByPlayerSuccess() {
        when(playerRepository.findById(anyLong()))
                .thenReturn(Optional.of(PlayerBuilder.aPlayerBuilder().build()));
        when(accountRepository.findAccountByPlayer(any(Player.class)))
                .thenReturn(Optional.of(AccountBuilder.anAccountBuilder().build()));
        assertDoesNotThrow(() -> accountService.findAccountByPlayer(1L));
    }

    @Test
    void testBind() {
        when(playerRepository.save(any(Player.class))).thenReturn(PlayerBuilder.aPlayerBuilder().build());
        when(accountRepository.save(any(Account.class))).thenReturn(AccountBuilder.anAccountBuilder().build());
        Player player = PlayerBuilder.aPlayerBuilder().build();
        assertNull(player.getAccount());
        accountService.bind(player);
        assertNotNull(player.getAccount());
    }

    @Test
    void testReplenishAccountSuccess() {
        final double moneyAmount = 10;
        Account account = AccountBuilder.anAccountBuilder().build();
        Transaction transaction = TransactionBuilder.aTransactionBuilder().withMoneyAmount(moneyAmount).withAccount(account).build();
        TransactionRequest request = mock(TransactionRequest.class);
        when(transactionService.addTransaction(request)).thenReturn(transaction);
        doNothing().when(accountRepository).updateAccount(anyDouble(), anyLong());
        Transaction result = accountService.replenish(request);
        assertEquals(moneyAmount, result.getMoneyAmount());
        assertEquals(account, result.getAccount());
    }

    @Test
    void testWithdrawAccountSuccess() {
        final double moneyAmount = 10;
        Account account = AccountBuilder.anAccountBuilder().withCurrentMoneyAmount(moneyAmount + 1).build();
        Transaction transaction = TransactionBuilder.aTransactionBuilder().withMoneyAmount(moneyAmount).withAccount(account).build();
        TransactionRequest request = mock(TransactionRequest.class);
        when(transactionService.addTransaction(request)).thenReturn(transaction);
        doNothing().when(accountRepository).updateAccount(anyDouble(), anyLong());
        assertDoesNotThrow(() -> accountService.withdraw(request));
    }

    @Test
    void testWithdrawAccountNotEnoughMoney() {
        final double moneyAmount = 10;
        Account account = AccountBuilder.anAccountBuilder().withCurrentMoneyAmount(moneyAmount - 1).build();
        Transaction transaction = TransactionBuilder.aTransactionBuilder().withMoneyAmount(moneyAmount).withAccount(account).build();
        TransactionRequest request = mock(TransactionRequest.class);
        when(transactionService.addTransaction(request)).thenReturn(transaction);
        doNothing().when(accountRepository).updateAccount(anyDouble(), anyLong());
        assertThatThrownBy(() -> accountService.withdraw(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("cannot withdraw bigger money amount that the account has");
    }
}