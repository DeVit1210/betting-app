package com.betting.bets.coupon.executed;

import com.betting.bets.coupon.Coupon;
import com.betting.bets.stake.StakeOutcome;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ExecutedCoupon extends Coupon {
    private Coupon coupon;
    private StakeOutcome outcome;
    private double moneyWon;

    public ExecutedCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}
