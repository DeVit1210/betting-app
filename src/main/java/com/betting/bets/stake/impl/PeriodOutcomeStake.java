package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.Outcome;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static com.betting.bets.stake.StakeOutcome.LOSE;
import static com.betting.bets.stake.StakeOutcome.WIN;
@Entity
@NoArgsConstructor
public class PeriodOutcomeStake extends OutcomeStake {
    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return PeriodOutcomeStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }
    @Override
    public void resolveOutcome(EventResults eventResults) {
        ResultPair periodSummary = eventResults.getBothOpponentsSummary(this.getResultType());
        Outcome periodOutcome = this.getOutcomeByResultPair(periodSummary);
        this.stakeOutcome = periodOutcome.equals(getOutcomeByPrefix(this.getName())) ? WIN : LOSE;
    }

    @Builder
    public PeriodOutcomeStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
}
