package com.betting.bets.coupon;

import com.betting.bets.stake.Stake;
import com.betting.user.player.account.Account;
import com.betting.user.player.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.betting.bets.stake.StakeOutcome.*;

@Component
@RequiredArgsConstructor
public class CouponHandler {
    private final AccountService accountService;

    public void countWinnings(Coupon coupon) {
        List<Stake> stakeList = coupon.getStakeList();
        boolean anyStakeLost = stakeList.stream().anyMatch(stake -> stake.getStakeOutcome().equals(LOSE));
        if (anyStakeLost) {
            coupon.setMoneyWon(0.0);
            coupon.setOutcome(LOSE);
        } else {
            double returnStakesTotalFactor = getReturnStakesTotalFactor(stakeList);
            double moneyWon = coupon.getMoneyAmount() * (coupon.getTotalFactor() / returnStakesTotalFactor);
            coupon.setMoneyWon(moneyWon);
            coupon.setOutcome(WIN);
        }
    }

    public static double getReturnStakesTotalFactor(List<Stake> stakeList) {
        return stakeList.stream()
                .filter(stake -> stake.getStakeOutcome().equals(RETURN))
                .mapToDouble(Stake::getFactor)
                .reduce(1.0, (accumulator, factor) -> accumulator * factor);
    }

    public void updatePlayerAccount(Coupon coupon) {
        Account account = coupon.getPlayer().getAccount();
        accountService.replenish(account, coupon.getMoneyWon());
    }
}
