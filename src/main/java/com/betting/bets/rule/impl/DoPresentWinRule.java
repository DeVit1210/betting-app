package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.Expression;
import com.betting.bets.rule.expression.IsPresentExpression;
import com.betting.bets.stake.StakeOutcome;

public class DoPresentWinRule implements Rule {
    @Override
    public boolean evaluate(Expression expression) {
        IsPresentExpression isPresentExpression = (IsPresentExpression) expression;
        return isPresentExpression.getTotalSummary() > 0 && isPresentExpression.getStakeName().contains("Yes");
    }

    @Override
    public StakeOutcome getOutcome() {
        return StakeOutcome.WIN;
    }
}
