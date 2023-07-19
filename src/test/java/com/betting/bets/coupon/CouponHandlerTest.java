package com.betting.bets.coupon;

import com.betting.bets.stake.Stake;
import com.betting.cloner.impl.CouponCloner;
import com.betting.test_builder.impl.AccountBuilder;
import com.betting.test_builder.impl.CouponBuilder;
import com.betting.test_builder.impl.StakeBuilder;
import com.betting.user.player.Player;
import com.betting.user.player.account.Account;
import com.betting.user.player.account.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.betting.bets.stake.StakeOutcome.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class CouponHandlerTest {
    @InjectMocks
    private CouponHandler couponHandler;
    @Mock
    private AccountService accountService;
    private final StakeBuilder stakeBuilder = StakeBuilder.aStakeBuilder();

    @Test
    void testGetReturnStakesTotalFactorEmpty() {
        List<Stake> stakeList = Collections.nCopies(5, stakeBuilder.withStakeOutcome(getAnyBut(RETURN)).build());
        double result = CouponHandler.getReturnStakesTotalFactor(stakeList);
        assertEquals(1.0, result);
    }

    @Test
    void testGetReturnStakesTotalFactorSuccess() {
        Random random = new Random();
        List<Float> stakeFactorList = new ArrayList<>();
        IntStream.range(0, 5).forEach(value -> stakeFactorList.add(random.nextFloat()));
        List<Stake> stakeList = new ArrayList<>();
        stakeFactorList.forEach(factor -> stakeList.add(stakeBuilder.withStakeOutcome(RETURN).withFactor(factor).build()));
        float result = (float) CouponHandler.getReturnStakesTotalFactor(stakeList);
        assertEquals(stakeFactorList.stream().reduce(1.0f, (sum, current) -> sum * current), result);
    }

    @Test
    void testUpdatePLayerAccountSuccess() {
        final double accountMoneyAmount = 10.0;
        final double couponMoneyWon = 10.0;
        Account account = AccountBuilder.anAccountBuilder().withCurrentMoneyAmount(accountMoneyAmount).build();
        Player player = mock(Player.class);
        when(player.getAccount()).thenReturn(account);
        Coupon coupon = CouponBuilder.aCouponBuilder().withMoneyWon(couponMoneyWon).withPlayer(player).build();
        doNothing().when(accountService).replenish(any(Account.class), anyDouble());
        couponHandler.updatePlayerAccount(coupon);
        verify(accountService, times(1)).replenish(account, couponMoneyWon);
    }

    @Test
    void testCountWinningsAtLeastOneStakeLost() {
        List<Stake> stakeList = new ArrayList<>(
                Collections.nCopies(5, StakeBuilder.aStakeBuilder().withStakeOutcome(WIN).build())
        );
        stakeList.add(StakeBuilder.aStakeBuilder().withStakeOutcome(LOSE).build());
        Coupon coupon = CouponBuilder.aCouponBuilder().withStakeList(stakeList).build();
        couponHandler.countWinnings(coupon);
        assertEquals(0.0, coupon.getMoneyWon());
        assertEquals(LOSE, coupon.getOutcome());
    }

    @Test
    void testCountWinningsSuccess() {
        Random random = new Random();
        List<Stake> stakeList = new ArrayList<>(
                Collections.nCopies(5, StakeBuilder.aStakeBuilder()
                        .withStakeOutcome(WIN)
                        .withFactor(random.nextFloat())
                        .build())
        );
        Coupon coupon = CouponBuilder.aCouponBuilder()
                .withStakeList(stakeList)
                .withMoneyAmount(10)
                .build();
        Coupon clonedCoupon = CouponCloner.aCouponCloner().clone(coupon);
        couponHandler.countWinnings(coupon);
        // FIXME: 19.07.2023 
        assertNotEquals(clonedCoupon.getMoneyWon(), coupon.getMoneyWon());
        assertEquals(WIN, coupon.getOutcome());
    }
}