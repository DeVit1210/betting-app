package com.betting.bets.coupon;

import com.betting.bets.stake.Stake;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class CouponSpec {
    private List<Stake> stakeList;
    private LocalDateTime timeOfMaking;
    private double moneyAmount;
    private double totalFactor;
}
