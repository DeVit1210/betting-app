package com.betting.bets.stake.impl;

import com.betting.bets.rule.engine.IsPresentRuleEngine;
import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.rule.expression.IsPresentExpression;
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
public class IsPresentStake extends Stake {
    @Override
    public void resolveOutcome(EventResults eventResults) {
        int totalSummary = eventResults.getBothOpponentsSummary(this.getResultType()).sum();
        RuleEngine ruleEngine = new IsPresentRuleEngine();
        this.stakeOutcome = ruleEngine.process(new IsPresentExpression(this.getName(), totalSummary));
    }
    @Builder
    public IsPresentStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return IsPresentStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }
}
