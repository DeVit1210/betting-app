package com.betting.bets.coupon;

import com.betting.bets.stake.Stake;
import com.betting.events.betting_entity.BettingEntity;
import com.betting.user.player.Player;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Coupon implements BettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime timeOfMaking;
    @ManyToMany
    @JoinTable(name = "bet_stake",
            joinColumns = @JoinColumn(name = "bet_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "stake_id", referencedColumnName = "id"))
    protected List<Stake> stakeList;
    @ManyToOne
    private Player player;
    protected double moneyAmount;
    private double totalFactor;
    private boolean isExecuted;
}
