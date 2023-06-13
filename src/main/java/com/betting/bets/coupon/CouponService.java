package com.betting.bets.coupon;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeService;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final PlayerService playerService;
    private final StakeService stakeService;
    private final CouponHandler couponHandler;

    public Coupon createCoupon(Long playerId) {
        Player player = playerService.findById(playerId);
        Coupon coupon = new Coupon();
        coupon.setPlayer(player);
        couponRepository.save(coupon);
        return coupon;
    }

    public Coupon findById(Long couponId) {
        return couponRepository.findById(couponId).orElseThrow(() -> new EntityNotFoundException(Coupon.class));
    }

    @Scheduled(cron = "0 * * * * ?")
    public void changeState() {
        List<Coupon> confirmedCoupons = couponRepository.findAllByState(CouponState.CONFIRMED)
                .filter(coupon -> coupon.getStakeList().stream().allMatch(Stake::isExecuted))
                .toList();
        confirmedCoupons.forEach(this::handleCoupon);
        this.saveAll(confirmedCoupons);
    }
    public void handleCoupon(Coupon coupon) {
        couponHandler.countWinnings(coupon);
        couponHandler.updatePlayerAccount(coupon);
        coupon.setState(CouponState.CLOSED);
    }

    private void saveAll(List<Coupon> coupons) {
        couponRepository.saveAll(coupons);
    }

    public Coupon addCouponStake(Long couponId, Long stakeId) {
        Stake stake = stakeService.findById(stakeId);
        Coupon coupon = this.findById(couponId);
        coupon.getStakeList().add(stake);
        couponRepository.save(coupon);
        return coupon;
    }

    public Coupon removeCouponStake(Long couponId, Long stakeId) {
        Stake stake = stakeService.findById(stakeId);
        Coupon coupon = this.findById(couponId);
        coupon.getStakeList().remove(stake);
        couponRepository.save(coupon);
        return coupon;
    }

    public Coupon confirmCoupon(Long couponId, double moneyAmount) {
        Coupon coupon = this.findById(couponId);
        coupon.setConfirmedAt(LocalDateTime.now());
        coupon.setState(CouponState.CONFIRMED);
        coupon.setMoneyAmount(moneyAmount);
        coupon.setTotalFactor(coupon.getStakeList().stream()
                .mapToDouble(Stake::getFactor)
                .reduce(1.0, (accumulator, factor) -> accumulator * factor)
        );
        couponRepository.save(coupon);
        return coupon;
    }
}
