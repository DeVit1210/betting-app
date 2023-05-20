package com.betting.bets.coupon.executed;

import com.betting.bets.stake.Stake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.betting.bets.stake.StakeOutcome.*;

@Service
@RequiredArgsConstructor
public class ExecutedCouponService {
    private final ExecutedCouponRepository executedCouponRepository;

    public void countWinnings(ExecutedCoupon executedCoupon) {
        List<Stake> stakeList = executedCoupon.getCoupon().getStakeList();
        boolean anyStakeLost = stakeList.stream().anyMatch(stake -> stake.getStakeOutcome().equals(LOSE));
        if (anyStakeLost) {
            executedCoupon.setMoneyWon(0.0);
            executedCoupon.setOutcome(LOSE);
        } else {
            double returnStakesTotalFactor = stakeList.stream()
                    .filter(stake -> stake.getStakeOutcome().equals(RETURN))
                    .mapToDouble(Stake::getFactor)
                    .reduce(1.0, (accumulator, factor) -> accumulator * factor);
            double moneyWon = executedCoupon.getCoupon().getMoneyAmount() *
                    (executedCoupon.getCoupon().getTotalFactor() / returnStakesTotalFactor);
            executedCoupon.setMoneyWon(moneyWon);
            executedCoupon.setOutcome(WIN);
        }
    }
}