package com.betting.user.player.account;

import com.betting.events.betting_entity.BettingEntity;
import com.betting.user.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Account implements BettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double currentMoneyAmount;
    @OneToOne
    private Player player;
    public Account(Player player) {
        this.player = player;
        this.currentMoneyAmount = 0.0;
    }
}
