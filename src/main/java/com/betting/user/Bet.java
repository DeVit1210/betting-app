package com.betting.user;

import com.betting.user.player.Player;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "bets")
public class Bet {
    @Id
    private Long Id;
    @ManyToOne
    private Player player;
}
