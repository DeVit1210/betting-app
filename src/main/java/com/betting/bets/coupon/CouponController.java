package com.betting.bets.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/add/{playerId}")
    public ResponseEntity<Coupon> addCoupon(@PathVariable Long playerId, @RequestBody CouponRequest request) {
        return ResponseEntity.ok(couponService.createCoupon(playerId, request));
    }
}
