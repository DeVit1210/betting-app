package com.betting.bets.coupon;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.events.betting_entity.BettingEntity;
import com.betting.user.player.Player;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Coupon implements BettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime confirmedAt;
    @ManyToMany
    @JoinTable(name = "coupon_stake",
            joinColumns = @JoinColumn(name = "coupon_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "stake_id", referencedColumnName = "id"))
    private List<Stake> stakeList;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    private double moneyAmount;
    private double totalFactor;
    @Enumerated(EnumType.STRING)
    private CouponState state;
    @Enumerated(EnumType.ORDINAL)
    private StakeOutcome outcome;
    private double moneyWon;
}
