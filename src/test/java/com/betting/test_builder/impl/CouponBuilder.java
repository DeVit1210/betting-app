package com.betting.test_builder.impl;

import com.betting.bets.coupon.Coupon;
import com.betting.bets.coupon.CouponState;
import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.test_builder.TestBuilder;
import com.betting.user.player.Player;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(staticName = "aCouponBuilder")
@AllArgsConstructor
@With
public class CouponBuilder implements TestBuilder<Coupon> {
    private LocalDateTime confirmedAt = LocalDateTime.now();
    private List<Stake> stakeList = new ArrayList<>();
    private Player player = null;
    private double moneyAmount = 0.0;
    private CouponState couponState = CouponState.NOT_CONFIRMED;
    private StakeOutcome stakeOutcome = StakeOutcome.RETURN;
    private double moneyWon = 0.0;

    @Override
    public Coupon build() {
        return Coupon.builder()
                .confirmedAt(confirmedAt)
                .stakeList(stakeList)
                .player(player)
                .moneyAmount(moneyAmount)
                .totalFactor(stakeList.stream().mapToDouble(Stake::getFactor).reduce(1.0, (acc, cur) -> acc * cur))
                .state(couponState)
                .outcome(stakeOutcome)
                .moneyWon(moneyWon)
                .build();
    }
}
