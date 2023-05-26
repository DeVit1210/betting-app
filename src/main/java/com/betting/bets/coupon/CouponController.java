package com.betting.bets.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/add/{playerId}")
    public ResponseEntity<Coupon> addCoupon(@PathVariable Long playerId) {
        return ResponseEntity.ok(couponService.createCoupon(playerId));
    }

    @PostMapping("/addStake")
    public ResponseEntity<Coupon> addCouponStake(@RequestParam UUID couponId, @RequestParam Long stakeId) {
        return ResponseEntity.ok(couponService.addCouponStake(couponId, stakeId));
    }

    @PostMapping("/removeStake")
    public ResponseEntity<Coupon> removeCouponStake(@RequestParam UUID couponId, @RequestParam Long stakeId) {
        return ResponseEntity.ok(couponService.removeCouponStake(couponId, stakeId));
    }

    @PostMapping("/confirm/")
    public ResponseEntity<Coupon> confirmCoupon(@RequestParam UUID couponId, @RequestParam double moneyAmount) {
        return ResponseEntity.ok(couponService.confirmCoupon(couponId, moneyAmount));
    }
}
