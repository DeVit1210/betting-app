package com.betting.bets.stake.impl;

import com.betting.bets.rule.engine.DoubleOutcomeRuleEngine;
import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.rule.expression.OutcomeExpression;
import com.betting.bets.stake.Stake;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.Outcome;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class PeriodDoubleStake extends DoubleOutcomeStake {
    @Builder(builderMethodName = "derivedBuilder")
    public PeriodDoubleStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public void resolveOutcome(EventResults eventResults) {
        ResultPair periodResults = eventResults.getBothOpponentsSummary(this.getResultType());
        Outcome periodOutcome = this.getOutcomeByResultPair(periodResults);
        RuleEngine ruleEngine = new DoubleOutcomeRuleEngine();
        this.stakeOutcome = ruleEngine.process(new OutcomeExpression(this.getName(), periodOutcome));
    }

    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return PeriodBothScoreStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }
}
