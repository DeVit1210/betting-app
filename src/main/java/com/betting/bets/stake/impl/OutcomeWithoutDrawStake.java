package com.betting.bets.stake.impl;

import com.betting.bets.rule.engine.OutcomeRuleEngine;
import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.rule.expression.OutcomeExpression;
import com.betting.bets.stake.Stake;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.type.Outcome;
import com.betting.results.type.ResultType;
import lombok.Builder;

public class OutcomeWithoutDrawStake extends Stake {
    @Builder
    public OutcomeWithoutDrawStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public void resolveOutcome(EventResults eventResults) {
        String stakeWinner = this.getName();
        Outcome currentOutcome = eventResults.outcome();
        RuleEngine ruleEngine = new OutcomeRuleEngine();
        this.stakeOutcome = ruleEngine.process(new OutcomeExpression(stakeWinner, currentOutcome));
    }
    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return OutcomeWithoutDrawStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }
}
