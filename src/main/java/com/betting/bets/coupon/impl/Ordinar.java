package com.betting.bets.coupon.impl;

import com.betting.bets.coupon.Coupon;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Ordinar extends Coupon {
    @Override
    public void handleStakes() {
        if (stakeList.size() > 1) {
            throw new IllegalStateException("the number of stakes can't be equal to 1 in the ordinar!");
        }

    }
}
