package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.Expression;
import com.betting.bets.rule.expression.WentFullDistanceExpression;
import com.betting.bets.stake.StakeOutcome;
import com.betting.results.type.FightResultType;

public class WentFullDistanceLoseRule implements Rule {
    @Override
    public boolean evaluate(Expression expression) {
        WentFullDistanceExpression wentFullDistanceExpression = (WentFullDistanceExpression) expression;
        return wentFullDistanceExpression.getStakeName().contains("No")
                && wentFullDistanceExpression.getFightResultType().equals(FightResultType.DECISION);
    }
    @Override
    public StakeOutcome getOutcome() {
        return StakeOutcome.LOSE;
    }
}
