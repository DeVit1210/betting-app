package com.betting.bets.stake.impl;

import com.betting.bets.rule.engine.BothScoreRuleEngine;
import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.rule.expression.BothScoreExpression;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake_type.StakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class BothScoreAndDoubleOutcomeStake extends DoubleOutcomeStake {
    public BothScoreAndDoubleOutcomeStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }

    @Override
    public void resolveOutcome(EventResults eventResults) {
        ResultPair score = eventResults.getOpponentsPoints();
        RuleEngine ruleEngine = new BothScoreRuleEngine();
        StakeOutcome bothScoreStakeOutcome = ruleEngine.process(new BothScoreExpression(this.getName(), score));
        if(bothScoreStakeOutcome.equals(StakeOutcome.WIN)) {
            super.resolveOutcome(eventResults);
        }
        else this.stakeOutcome = StakeOutcome.LOSE;
    }
}
