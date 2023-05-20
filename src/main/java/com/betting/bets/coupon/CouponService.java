package com.betting.bets.coupon;

import com.betting.mapping.CouponRequestMapper;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.scheduling.annotation.Scheduled;
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
        Coupon coupon = mapper.mapFrom(request);
        coupon.setPlayer(player);
        couponRepository.save(coupon);
        return coupon;
    }

    @Scheduled(cron = "0 * * * * ?")
    public void handleCoupons() {
        System.out.println("Coupons handling...");
        couponRepository.findAllByExecutedIsTrue().forEach(coupon -> {

        });
    }
}
