package com.betting.bets.stake.impl;

import com.betting.bets.rule.engine.DoubleOutcomeRuleEngine;
import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.rule.expression.OutcomeExpression;
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
public class DoubleOutcomeStake extends OutcomeStake {
    @Builder
    public DoubleOutcomeStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return DoubleOutcomeStake.builder().name(name).stakeType(stakeType).build();
    }
    @Override
    public void resolveOutcome(EventResults eventResults) {
        RuleEngine ruleEngine = new DoubleOutcomeRuleEngine();
        this.stakeOutcome = ruleEngine.process(new OutcomeExpression(this.getName(), eventResults.outcome()));
    }


}
