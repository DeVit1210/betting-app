package com.betting.bets.coupon;

import com.betting.bets.stake.Stake;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.test_builder.impl.CouponBuilder;
import com.betting.test_builder.impl.StakeBuilder;
import com.betting.user.player.Player;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.betting.bets.coupon.CouponState.CONFIRMED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc(addFilters = false)
class CouponControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CouponService couponService;
    @Value("${test.exception.player-not-found}")
    private String playerNotfoundMessage;

    @Test
    void testAddCoupon() throws Exception {
        Coupon coupon = CouponBuilder.aCouponBuilder().build();
        when(couponService.createCoupon(anyLong())).thenReturn(coupon);
        mockMvc.perform(post("/add/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stakeList", Matchers.hasSize(0)));
    }

    @Test
    void testAddCouponPlayerNotFound() throws Exception {
        when(couponService.createCoupon(anyLong())).thenThrow(new EntityNotFoundException(Player.class));
        mockMvc.perform(post("/add/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), playerNotfoundMessage));

    }

    @Test
    void testAddCouponStakeSuccess() throws Exception {
        Stake stake = StakeBuilder.aStakeBuilder().build();
        Coupon coupon = CouponBuilder.aCouponBuilder().withStakeList(Collections.singletonList(stake)).build();
        when(couponService.addCouponStake(anyLong(), anyLong())).thenReturn(coupon);
        mockMvc.perform(post("/addStake")
                        .param("couponId", String.valueOf(1L))
                        .param("stakeId", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stakeList", Matchers.hasSize(1)));
    }

    @Test
    void testRemoveCouponStakeSuccess() throws Exception {
        Coupon coupon = CouponBuilder.aCouponBuilder().build();
        when(couponService.removeCouponStake(anyLong(), anyLong())).thenReturn(coupon);
        mockMvc.perform(post("/removeStake")
                        .param("couponId", String.valueOf(1L))
                        .param("stakeId", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stakeList", Matchers.hasSize(0)));
    }

    @Test
    void testConfirmCouponSuccess() throws Exception {
        final double moneyAmount = 10.0;
        Coupon coupon = CouponBuilder.aCouponBuilder().withCouponState(CONFIRMED).withMoneyAmount(moneyAmount).build();
        when(couponService.confirmCoupon(anyLong(), anyDouble())).thenReturn(coupon);
        mockMvc.perform(post("/confirm")
                        .param("couponId", String.valueOf(1L))
                        .param("moneyAmount", String.valueOf(moneyAmount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state", Matchers.matchesPattern(CONFIRMED.name())));
    }

}