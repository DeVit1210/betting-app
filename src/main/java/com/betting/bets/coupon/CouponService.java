package com.betting.bets.coupon;

import com.betting.mapping.CouponRequestMapper;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final PlayerService playerService;
    private final BeanFactory beanFactory;

    public Coupon createCoupon(Long playerId, CouponRequest request) {
        Player player = playerService.findById(playerId);
        CouponRequestMapper mapper = beanFactory.getBean(CouponRequestMapper.class);
        CouponSpec couponSpecification = mapper.mapFrom(request);
        Coupon coupon = CouponFactory.createCoupon(request.couponType(), couponSpecification);
        coupon.setPlayer(player);
        couponRepository.save(coupon);
        return coupon;
    }
}
