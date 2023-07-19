package com.betting.bets.coupon;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeService;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.test_builder.impl.CouponBuilder;
import com.betting.test_builder.impl.PlayerBuilder;
import com.betting.test_builder.impl.StakeBuilder;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class CouponServiceTest {
    @InjectMocks
    private CouponService couponService;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CouponHandler couponHandler;
    @Mock
    private StakeService stakeService;
    @Mock
    private PlayerService playerService;
    @Value("${test.exception.coupon-not-found}")
    private String couponNotFoundMessage;
    @Value("${test.exception.stake-not-found}")
    private String stakeNotFoundMessage;

    @Test
    void testFindByIdSuccess() {
        when(couponRepository.findById(anyLong())).thenReturn(Optional.of(CouponBuilder.aCouponBuilder().build()));
        assertDoesNotThrow(() -> couponService.findById(1L));
    }

    @Test
    void testFindByIdNotFound() {
        when(couponRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> couponService.findById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(couponNotFoundMessage);
    }

    @Test
    void testCreateCouponWrongPlayerId() {
        when(playerService.findById(anyLong())).thenThrow(EntityNotFoundException.class);
        assertThatThrownBy(() -> couponService.createCoupon(1L)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testCreateCouponSuccess() {
        Player player = PlayerBuilder.aPlayerBuilder().build();
        when(playerService.findById(anyLong())).thenReturn(player);
        Coupon coupon = couponService.createCoupon(1L);
        assertEquals(player, coupon.getPlayer());
    }

    @Test
    void testAddCouponStakeSuccess() {
        Stake stake = StakeBuilder.aStakeBuilder().build();
        when(stakeService.findById(anyLong())).thenReturn(stake);
        Coupon coupon = CouponBuilder.aCouponBuilder().build();
        assertEquals(0, coupon.getStakeList().size());
        when(couponRepository.findById(anyLong())).thenReturn(Optional.of(coupon));
        couponService.addCouponStake(1L, 1L);
        assertEquals(1, coupon.getStakeList().size());
    }

    @Test
    void testAddCouponStakeNotFoundStake() {
        when(stakeService.findById(anyLong())).thenThrow(new EntityNotFoundException(Stake.class));
        assertThatThrownBy(() -> couponService.addCouponStake(1L, 1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(stakeNotFoundMessage);
    }

    @Test
    void testRemoveCouponStakeSuccess() {
        final int initialStakeQuantity = 5;
        StakeBuilder stakeBuilder = StakeBuilder.aStakeBuilder();
        List<Stake> stakeList = new ArrayList<>(Collections.nCopies(initialStakeQuantity, stakeBuilder.build()));
        Coupon coupon = CouponBuilder.aCouponBuilder().withStakeList(stakeList).build();
        assertEquals(initialStakeQuantity, coupon.getStakeList().size());
        when(stakeService.findById(anyLong())).thenReturn(stakeList.get(0));
        when(couponRepository.findById(anyLong())).thenReturn(Optional.of(coupon));
        couponService.removeCouponStake(1L, 1L);
        assertEquals(initialStakeQuantity - 1, coupon.getStakeList().size());
    }


}