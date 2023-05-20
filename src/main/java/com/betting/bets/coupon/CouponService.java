package com.betting.bets.coupon;

import com.betting.bets.coupon.executed.ExecutedCoupon;
import com.betting.bets.coupon.executed.ExecutedCouponService;
import com.betting.bets.stake.Stake;
import com.betting.mapping.CouponRequestMapper;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import com.betting.user.player.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final ExecutedCouponService executedCouponService;
    private final PlayerService playerService;
    private final AccountService accountService;
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
        List<Coupon> couponsWithResolvedStakes = couponRepository.findAll().stream()
                .filter(coupon -> coupon.getStakeList().stream().allMatch(Stake::isExecuted))
                .toList();
        List<ExecutedCoupon> executedCoupons = couponsWithResolvedStakes.stream().map(ExecutedCoupon::new).toList();
        executedCoupons.forEach(executedCouponService::countWinnings);
        executedCoupons.forEach(executedCouponService::updatePlayerAccount);
        executedCouponService.saveAll(executedCoupons);
        couponRepository.deleteAll(couponsWithResolvedStakes);
    }
}
