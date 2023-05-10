package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.type.FightResultType;
import com.betting.results.type.Outcome;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static com.betting.results.type.FightResultType.*;
@Entity
@NoArgsConstructor
public class WinnerAndFightResultTypeStake extends FightOutcomeStake {
    @Builder
    public WinnerAndFightResultTypeStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }

    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return WinnerAndFightResultTypeStake.builder()
                .name(name).stakeType(stakeType).resultType(resultType)
                .build();
    }

    @Override
    public void resolveOutcome(EventResults eventResults) {
        int fightResultIndex = eventResults.getSummary(ResultType.FIGHT_ENDING);
        FightResultType fightResult = getByIndex(fightResultIndex);
        String stakeFightResult = this.getName().substring(this.getName().indexOf("/") + 1);
        if(fightResult.equals(DECISION) && stakeFightResult.equals(DECISION.getStakeOutcomeName())) {
            Outcome outcome = eventResults.outcome();
            String stakeOutcomePrefix = this.getName().substring(0, this.getName().indexOf("/"));
            Outcome stakeOutcome = this.getOutcomeByPrefix(stakeOutcomePrefix);
            this.stakeOutcome = outcome.equals(stakeOutcome) ? StakeOutcome.WIN : StakeOutcome.LOSE;
        } else {
            FightResultType stakeFightResultType = this.resolveFightResult(this.getName());
            this.stakeOutcome = fightResult.equals(stakeFightResultType) ? StakeOutcome.WIN : StakeOutcome.LOSE;
        }
    }
}
