package com.betting.bets.coupon;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.test_builder.impl.CouponBuilder;
import com.betting.test_builder.impl.StakeBuilder;
import com.betting.user.player.account.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

@SpringBootTest
class CouponHandlerTest {
    @InjectMocks
    private CouponHandler couponHandler;
    @Mock
    private AccountService accountService;

    @Test
    void testGetReturnStakesTotalFactorEmpty() {
        List<Stake> stakeList = Collections.nCopies(5, StakeBuilder.aStakeBuilder()
                .withStakeOutcome(StakeOutcome.getAnyBut(StakeOutcome.RETURN))
                .build()
        );
        Coupon coupon = CouponBuilder.aCouponBuilder().withStakeList(stakeList).build();
        couponHandler.countWinnings(coupon);

    }
}