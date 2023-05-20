package com.betting.mapping;

import com.betting.bets.coupon.Coupon;
import com.betting.bets.coupon.CouponRequest;
import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Scope("prototype")
public class CouponRequestMapper implements ObjectMapper<CouponRequest, Coupon> {
    private final StakeService stakeService;

    @Override
    public Coupon mapFrom(CouponRequest request) {
        List<Stake> stakeList = request.stakeIdList().stream().map(stakeService::findById).toList();
        return Coupon.builder()
                .stakeList(stakeList)
                .timeOfMaking(LocalDateTime.now())
                .moneyAmount(request.moneyToBet())
                .totalFactor(stakeList.stream()
                        .mapToDouble(Stake::getFactor)
                        .reduce(1.0, (acc, value) -> acc * value))
                .build();
    }
}
