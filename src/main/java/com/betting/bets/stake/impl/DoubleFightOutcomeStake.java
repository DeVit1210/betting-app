package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.type.FightResultType;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class DoubleFightOutcomeStake extends FightOutcomeStake {
    @Builder
    public DoubleFightOutcomeStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return DoubleFightOutcomeStake.builder().name(name).stakeType(stakeType).build();
    }

    @Override
    public void resolveOutcome(EventResults eventResults) {
        int fightResultIndex = eventResults.getSummary(this.getResultType());
        FightResultType fightResult = FightResultType.getByIndex(fightResultIndex);
        String fightResultWithoutWinner = fightResult.getStakeOutcomeName().substring(
                fightResult.getStakeOutcomeName().indexOf("/") + 1);
        if (this.getName().contains(fightResultWithoutWinner)) {
            this.stakeOutcome = StakeOutcome.WIN;
        }
        else this.stakeOutcome = StakeOutcome.LOSE;
    }
}
