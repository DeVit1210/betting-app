package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake_type.StakeType;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.Outcome;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class PeriodAndMatchOutcomeStake extends OutcomeStake {
    @Builder
    public PeriodAndMatchOutcomeStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }

    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return PeriodAndMatchOutcomeStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }

    @Override
    public void resolveOutcome(EventResults eventResults) {
        ResultPair firstPeriodResults = eventResults.getBothOpponentsSummary(this.getResultType());
        Outcome periodOutcome = this.getOutcomeByResultPair(firstPeriodResults);
        Outcome matchOutcome = eventResults.outcome();
        String periodStakeResult = this.getName().substring(0, this.getName().indexOf("/"));
        String matchStakeResult = this.getName().substring(this.getName().indexOf("/")+1);
        Outcome firstPeriodStakeOutcome = this.getOutcomeByPrefix(periodStakeResult);
        Outcome matchStakeOutcome = this.getOutcomeByPrefix(matchStakeResult);
        if(periodOutcome.equals(firstPeriodStakeOutcome) && matchOutcome.equals(matchStakeOutcome)) {
            this.stakeOutcome = StakeOutcome.WIN;
        } else this.stakeOutcome = StakeOutcome.LOSE;
    }
}
