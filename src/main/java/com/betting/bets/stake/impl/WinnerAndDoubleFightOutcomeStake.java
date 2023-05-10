package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.type.Outcome;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class WinnerAndDoubleFightOutcomeStake extends DoubleFightOutcomeStake {
    @Builder(builderMethodName = "inheritedBuilder")
    public WinnerAndDoubleFightOutcomeStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public void resolveOutcome(EventResults eventResults) {
        Outcome fightOutcome = eventResults.outcome();
        Outcome stakeOutcome = this.getOutcomeByPrefix(this.getName().substring(0, this.getName().indexOf(":")));
        if(fightOutcome.equals(stakeOutcome)) {
            super.resolveOutcome(eventResults);
        }
        else this.stakeOutcome = StakeOutcome.LOSE;
    }

    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return WinnerAndDoubleFightOutcomeStake.builder()
                .name(name).stakeType(stakeType).resultType(resultType)
                .build();
    }
}
