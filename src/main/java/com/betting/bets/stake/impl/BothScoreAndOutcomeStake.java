package com.betting.bets.stake.impl;

import com.betting.bets.rule.engine.BothScoreRuleEngine;
import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.rule.expression.BothScoreExpression;
import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
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
public class BothScoreAndOutcomeStake extends OutcomeStake {
    @Builder
    public BothScoreAndOutcomeStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }

    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return BothScoreAndOutcomeStake.builder().name(name).stakeType(stakeType).build();
    }

    @Override
    public void resolveOutcome(EventResults eventResults) {
        ResultPair score = eventResults.getOpponentsPoints();
        RuleEngine ruleEngine = new BothScoreRuleEngine();
        StakeOutcome bothScoreStakeOutcome = ruleEngine.process(new BothScoreExpression(this.getName(), score));
        if(bothScoreStakeOutcome.equals(StakeOutcome.WIN)) {
            String matchOutcomePrefix = this.getName().substring(this.getName().indexOf("/") + 1);
            Outcome stakeMatchOutcome = this.getOutcomeByPrefix(matchOutcomePrefix);
            Outcome matchOutcome = this.getOutcomeByResultPair(score);
            if(matchOutcome.equals(stakeMatchOutcome)) {
                this.stakeOutcome = StakeOutcome.WIN;
            } else this.stakeOutcome = StakeOutcome.LOSE;
        }
        else this.stakeOutcome = StakeOutcome.LOSE;
    }
}
