package com.betting.user.player.account;

import com.betting.user.player.Player;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double currentMoneyAmount;
    @OneToOne
    private Player player;
}
