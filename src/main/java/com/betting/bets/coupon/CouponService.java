package com.betting.bets.coupon;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeService;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.user.player.Player;
import com.betting.user.player.PlayerService;
import com.betting.user.player.account.Account;
import com.betting.user.player.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.betting.bets.stake.StakeOutcome.*;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final PlayerService playerService;
    private final AccountService accountService;
    private final BeanFactory beanFactory;
    private final StakeService stakeService;

    public Coupon createCoupon(Long playerId) {
        Player player = playerService.findById(playerId);
        Coupon coupon = new Coupon();
//        CouponRequestMapper mapper = beanFactory.getBean(CouponRequestMapper.class);
//        Coupon coupon = mapper.mapFrom(request);
        coupon.setPlayer(player);
        couponRepository.save(coupon);
        return coupon;
    }

    public Coupon findById(UUID couponId) {
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
        this.countWinnings(coupon);
        this.updatePlayerAccount(coupon);
        coupon.setState(CouponState.CLOSED);
    }

    private void saveAll(List<Coupon> coupons) {
        couponRepository.saveAll(coupons);
    }

    private void countWinnings(Coupon coupon) {
        List<Stake> stakeList = coupon.getStakeList();
        boolean anyStakeLost = stakeList.stream().anyMatch(stake -> stake.getStakeOutcome().equals(LOSE));
        if (anyStakeLost) {
            coupon.setMoneyWon(0.0);
            coupon.setOutcome(LOSE);
        } else {
            double returnStakesTotalFactor = stakeList.stream()
                    .filter(stake -> stake.getStakeOutcome().equals(RETURN))
                    .mapToDouble(Stake::getFactor)
                    .reduce(1.0, (accumulator, factor) -> accumulator * factor);
            double moneyWon = coupon.getMoneyAmount() * (coupon.getTotalFactor() / returnStakesTotalFactor);
            coupon.setMoneyWon(moneyWon);
            coupon.setOutcome(WIN);
        }
    }

    private void updatePlayerAccount(Coupon coupon) {
        Account account = coupon.getPlayer().getAccount();
        accountService.replenish(account, coupon.getMoneyWon());
    }

    public Coupon addCouponStake(UUID couponId, Long stakeId) {
        Stake stake = stakeService.findById(stakeId);
        Coupon coupon = this.findById(couponId);
        coupon.getStakeList().add(stake);
        couponRepository.save(coupon);
        return coupon;
    }

    public Coupon removeCouponStake(UUID couponId, Long stakeId) {
        Stake stake = stakeService.findById(stakeId);
        Coupon coupon = this.findById(couponId);
        coupon.getStakeList().remove(stake);
        couponRepository.save(coupon);
        return coupon;
    }

    public Coupon confirmCoupon(UUID couponId, double moneyAmount) {
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
