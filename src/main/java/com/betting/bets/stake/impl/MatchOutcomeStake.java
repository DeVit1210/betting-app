package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static com.betting.bets.stake.StakeOutcome.LOSE;
import static com.betting.bets.stake.StakeOutcome.WIN;
@Entity
@NoArgsConstructor
public class MatchOutcomeStake extends OutcomeStake {
    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return MatchOutcomeStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }
    @Builder
    public MatchOutcomeStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public void resolveOutcome(EventResults eventResults) {
        this.stakeOutcome = eventResults.outcome().equals(getOutcomeByPrefix(this.getName())) ? WIN : LOSE;
    }

}
