package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake_type.StakeType;
import com.betting.results.EventResults;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class PeriodScoreStake extends ScoreStake {
    @Builder(builderMethodName = "inheritedBuilder")
    public PeriodScoreStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }

    @Override
    public void resolveOutcome(EventResults eventResults) {
        this.resolveScore(eventResults.getBothOpponentsSummary(this.getResultType()));
    }

    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return PeriodScoreStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }
}
