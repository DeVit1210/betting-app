package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.Expression;
import com.betting.bets.rule.expression.TotalExpression;
import com.betting.bets.stake.StakeOutcome;

public class TotalReturnRule implements Rule {
    @Override
    public boolean evaluate(Expression expression) {
        TotalExpression totalExpression = (TotalExpression) expression;
        return totalExpression.getTotalPointsQuantity() == totalExpression.getStakePointsQuantity();
    }
    @Override
    public StakeOutcome getOutcome() {
        return StakeOutcome.RETURN;
    }
}
