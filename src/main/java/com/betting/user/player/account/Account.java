package com.betting.user.player.account;

import com.betting.events.betting_entity.BettingEntity;
import com.betting.user.player.Player;
import com.betting.user.player.account.transaction.Transaction;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Account implements BettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double currentMoneyAmount;
    @OneToOne
    private Player player;
    @OneToMany(mappedBy = "account")
    List<Transaction> transactions;

    public Account(Player player) {
        this.player = player;
        this.currentMoneyAmount = 0.0;
    }
}
