package com.betting.bets.coupon;

import java.util.List;

public record CouponRequest(String couponType, List<Long> stakeIdList, double moneyToBet) {
}
