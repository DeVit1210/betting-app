package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.Outcome;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class StatsOutcomeStake extends OutcomeStake {
    @Builder
    public StatsOutcomeStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return StatsOutcomeStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }
    @Override
    public void resolveOutcome(EventResults eventResults) {
        ResultPair resultPair = eventResults.getBothOpponentsSummary(this.getResultType());
        Outcome statsOutcome = this.getOutcomeByResultPair(resultPair);
        Outcome stakeStatsOutcome = this.getOutcomeByPrefix(this.getName());
        if(stakeStatsOutcome.equals(statsOutcome)) {
            this.stakeOutcome = StakeOutcome.WIN;
        } else this.stakeOutcome = StakeOutcome.LOSE;
    }
}
