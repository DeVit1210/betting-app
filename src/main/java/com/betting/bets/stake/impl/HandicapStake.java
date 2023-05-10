package com.betting.bets.stake.impl;

import com.betting.bets.rule.engine.HandicapRuleEngine;
import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.rule.expression.HandicapExpression;
import com.betting.bets.stake.Stake;
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
public class HandicapStake extends Stake {
    @Override
    public void resolveOutcome(EventResults eventResults) {
        ResultPair resultPair = eventResults.getOpponentsPoints();
        int pointDifference = resultPair.getFirstOpponentResult() - resultPair.getSecondOpponentResult();
        String handicapPrefix = this.getName().substring(0, getName().indexOf(" "));
        float handicapValue = Integer.parseInt(this.getName().substring(this.getName().indexOf(" ")));
        RuleEngine engine = new HandicapRuleEngine();
        this.stakeOutcome = engine.process(new HandicapExpression(handicapPrefix, handicapValue, pointDifference));
    }
    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return HandicapStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }
    @Builder
    public HandicapStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
}
