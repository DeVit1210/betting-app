package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ScoreStake extends Stake {
    @Builder
    public ScoreStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public void resolveOutcome(EventResults eventResults) {
        this.stakeOutcome = this.resolveScore(eventResults.getOpponentsPoints());
    }

    ResultPair getResultPairByString(String result) {
        int firstOpponentResult = Integer.parseInt(result.substring(0, result.indexOf(":")));
        int secondOpponentResult = Integer.parseInt(result.substring(result.indexOf(":") + 1));
        return ResultPair.of(firstOpponentResult, secondOpponentResult);
    }

    StakeOutcome resolveScore(ResultPair scoreResultPair) {
        ResultPair stakeScoreResultPair = this.getResultPairByString(this.getName());
        return scoreResultPair.equals(stakeScoreResultPair) ? StakeOutcome.WIN : StakeOutcome.LOSE;
    }

    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return ScoreStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }
}
