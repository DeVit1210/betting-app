package com.betting.user.player.account.transaction;

import com.betting.test_builder.impl.TransactionBuilder;
import com.betting.user.player.account.Account;
import com.betting.user.player.account.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    private Account firstAccount = new Account();
    private Account secondAccount = new Account();

    @BeforeEach
    void setUp() {
        accountRepository.saveAll(List.of(firstAccount, secondAccount));
        TransactionBuilder transactionBuilder = TransactionBuilder.aTransactionBuilder();
        transactionRepository.save(transactionBuilder.withAccount(firstAccount).build());
        transactionRepository.save(transactionBuilder.withAccount(secondAccount).build());
    }

    @Test
    void testFindTransactionsByAccountSuccess() {
        System.out.println(transactionRepository.findAll().size());
        Streamable<Transaction> result = transactionRepository.findAllByAccount(firstAccount);
        assertEquals(2, transactionRepository.findAll().size());
        assertEquals(1, result.toList().size());
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }
}