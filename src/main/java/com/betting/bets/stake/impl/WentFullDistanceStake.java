package com.betting.bets.stake.impl;

import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.rule.engine.WentFullDistanceRuleEngine;
import com.betting.bets.rule.expression.WentFullDistanceExpression;
import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.type.FightResultType;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class WentFullDistanceStake extends FightOutcomeStake {
    @Builder
    public WentFullDistanceStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }
    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return WentFullDistanceStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }
    @Override
    public void resolveOutcome(EventResults eventResults) {
        int fightOutcomeIndex = eventResults.getSummary(this.getResultType());
        FightResultType fightResultType = FightResultType.getByIndex(fightOutcomeIndex);
        RuleEngine ruleEngine = new WentFullDistanceRuleEngine();
        this.stakeOutcome = ruleEngine.process(new WentFullDistanceExpression(this.getName(), fightResultType));
    }
}
