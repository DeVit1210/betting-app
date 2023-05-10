package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class SecondOpponentTotalStake extends TotalStake {
    @Builder
    public SecondOpponentTotalStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public void resolveOutcome(EventResults eventResults) {
        this.stakeOutcome = this.resolveTotalOutcome(
                eventResults.getSummary(ResultType.valueOf("SECOND_OPPONENT_" + this.getResultType().name()))
        );
    }

    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return SecondOpponentTotalStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }
}
