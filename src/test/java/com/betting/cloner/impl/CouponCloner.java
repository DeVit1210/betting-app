package com.betting.cloner.impl;

import com.betting.bets.coupon.Coupon;
import com.betting.cloner.TestCloner;
import lombok.NoArgsConstructor;

@NoArgsConstructor(staticName = "aCouponCloner")
public class CouponCloner implements TestCloner<Coupon> {
    @Override
    public Coupon clone(Coupon coupon) {
        return Coupon.builder()
                .outcome(coupon.getOutcome())
                .moneyWon(coupon.getMoneyWon())
                .state(coupon.getState())
                .moneyAmount(coupon.getMoneyAmount())
                .player(coupon.getPlayer())
                .stakeList(coupon.getStakeList())
                .totalFactor(coupon.getTotalFactor())
                .confirmedAt(coupon.getConfirmedAt())
                .build();
    }
}
