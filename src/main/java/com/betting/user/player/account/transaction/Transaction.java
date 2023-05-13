package com.betting.user.player.account.transaction;

import com.betting.user.player.account.Account;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Account account;
    LocalDateTime time;
    double moneyAmount;
    boolean moneyOut;
}
