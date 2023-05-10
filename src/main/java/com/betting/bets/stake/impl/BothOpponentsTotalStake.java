package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake_type.StakeType;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;
@Entity
@NoArgsConstructor
public class BothOpponentsTotalStake extends TotalStake {
    @Builder
    public BothOpponentsTotalStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public void resolveOutcome(EventResults eventResults) {
        ResultPair resultPair = eventResults.getBothOpponentsSummary(this.getResultType());
        int totalPointsQuantity = resultPair.getFirstOpponentResult() + resultPair.getSecondOpponentResult();
        this.stakeOutcome = this.resolveTotalOutcome(totalPointsQuantity);
    }
    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return BothOpponentsTotalStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }
}
