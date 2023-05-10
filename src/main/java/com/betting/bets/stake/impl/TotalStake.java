package com.betting.bets.stake.impl;

import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.rule.engine.TotalRuleEngine;
import com.betting.bets.rule.expression.TotalExpression;
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
public abstract class TotalStake extends Stake {
    public TotalStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }

    protected StakeOutcome resolveTotalOutcome(int totalPointsQuantity) {
        float stakePointsQuantity = Float.parseFloat(getName().substring(getName().indexOf(" ") + 1));
        boolean totalHigher = this.getName().contains("ТБ");
        RuleEngine ruleEngine = new TotalRuleEngine();
        return ruleEngine.process(new TotalExpression(totalHigher, stakePointsQuantity, totalPointsQuantity));
    }
}
