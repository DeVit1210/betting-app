package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ExactRoundsQuantityStake extends Stake {
    @Builder
    public ExactRoundsQuantityStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return ExactRoundsQuantityStake.builder().name(name).stakeType(stakeType).build();
    }

    @Override
    public void resolveOutcome(EventResults eventResults) {
        int roundsQuantity = eventResults.getSummary(this.getResultType());
        int stakeRoundsQuantity = Integer.parseInt(this.getName());
        this.stakeOutcome = roundsQuantity == stakeRoundsQuantity ? StakeOutcome.WIN : StakeOutcome.LOSE;
    }
}
