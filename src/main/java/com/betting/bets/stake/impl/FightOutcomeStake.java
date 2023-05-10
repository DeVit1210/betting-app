package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.FightResultType;
import com.betting.results.type.Outcome;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static com.betting.results.type.Outcome.*;
@Entity
@NoArgsConstructor
public abstract class FightOutcomeStake extends Stake {
    public FightOutcomeStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    Outcome getOutcomeByResultPair(ResultPair resultPair) {
        if(resultPair.getFirstOpponentResult() > 1) return FIRST_WIN;
        if(resultPair.getSecondOpponentResult() > 1) return SECOND_WIN;
        return DRAW;
    }
    FightResultType resolveFightResult(String stakeName) {
        return FightResultType.of(stakeName);
    }
    Outcome getOutcomeByPrefix(String prefix) {
        return Outcome.of(prefix);
    }
}
