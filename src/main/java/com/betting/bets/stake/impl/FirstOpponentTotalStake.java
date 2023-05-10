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
public class FirstOpponentTotalStake extends TotalStake {
    @Builder
    public FirstOpponentTotalStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return FirstOpponentTotalStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }
    @Override
    public void resolveOutcome(EventResults eventResults) {
        this.stakeOutcome = this.resolveTotalOutcome(
                eventResults.getSummary(ResultType.valueOf("FIRST_OPPONENT_" + this.getResultType().name()))
        );
    }
}
