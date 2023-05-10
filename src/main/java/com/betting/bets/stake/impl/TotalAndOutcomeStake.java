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
import com.betting.results.type.Outcome;
import com.betting.results.type.ResultType;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class TotalAndOutcomeStake extends OutcomeStake {
    @Builder
    public TotalAndOutcomeStake(String name, StakeType stakeType, ResultType resultType) {
        super(name, stakeType, resultType);
    }

    @Override
    public Stake build(String name, StakeType stakeType, ResultType resultType) {
        return TotalAndOutcomeStake.builder().name(name).stakeType(stakeType).resultType(resultType).build();
    }

    @Override
    public void resolveOutcome(EventResults eventResults) {
        ResultPair resultPair = eventResults.getOpponentsPoints();
        int totalPointsQuantity = resultPair.sum();
        float stakePointsQuantity = Float.parseFloat(getName().substring(getName().lastIndexOf(" ") + 1));
        boolean totalHigher = getName().contains("ТБ");
        RuleEngine ruleEngine = new TotalRuleEngine();
        StakeOutcome totalPartOfStakeOutcome =
                ruleEngine.process(new TotalExpression(totalHigher, stakePointsQuantity, totalPointsQuantity));
        Outcome outcome = eventResults.outcome();
        String stakeOutcomePrefix = this.getName().substring(0, this.getName().indexOf("/"));
        Outcome stakeOutcome = this.getOutcomeByPrefix(stakeOutcomePrefix);
        if(outcome.equals(stakeOutcome) && totalPartOfStakeOutcome.equals(StakeOutcome.WIN)) {
            this.stakeOutcome = StakeOutcome.WIN;
        } else this.stakeOutcome = StakeOutcome.LOSE;
    }
}
