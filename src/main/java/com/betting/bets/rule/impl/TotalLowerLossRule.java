package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.Expression;
import com.betting.bets.rule.expression.TotalExpression;
import com.betting.bets.stake.StakeOutcome;

public class TotalLowerLossRule implements Rule {
    @Override
    public boolean evaluate(Expression expression) {
        TotalExpression totalExpression = (TotalExpression) expression;
        return !totalExpression.isTotalHigher() &&
                totalExpression.getStakePointsQuantity() < totalExpression.getTotalPointsQuantity();
    }
    @Override
    public StakeOutcome getOutcome() {
        return StakeOutcome.LOSE;
    }
}
