package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.ResultPair;
import com.betting.results.type.Outcome;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static com.betting.results.type.Outcome.*;
@Entity
@NoArgsConstructor
public abstract class OutcomeStake extends Stake {
    public OutcomeStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    Outcome getOutcomeByPrefix(String prefix) {
        return Outcome.of(prefix);
    }
    Outcome getOutcomeByResultPair(ResultPair resultPair) {
        return resultPair.getFirstOpponentResult() > resultPair.getSecondOpponentResult() ? FIRST_WIN
                : (resultPair.getSecondOpponentResult() > resultPair.getFirstOpponentResult() ? SECOND_WIN : DRAW);
    }
}
