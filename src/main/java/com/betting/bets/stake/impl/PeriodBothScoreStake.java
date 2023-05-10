package com.betting.bets.stake.impl;

import com.betting.bets.rule.engine.BothScoreRuleEngine;
import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.rule.expression.BothScoreExpression;
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
public class PeriodBothScoreStake extends Stake {
    @Builder
    public PeriodBothScoreStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public void resolveOutcome(EventResults eventResults) {
        ResultPair score = eventResults.getBothOpponentsSummary(this.getResultType());
        RuleEngine ruleEngine = new BothScoreRuleEngine();
        this.stakeOutcome = ruleEngine.process(new BothScoreExpression(this.getName(), score));
    }
    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return PeriodBothScoreStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }
}
