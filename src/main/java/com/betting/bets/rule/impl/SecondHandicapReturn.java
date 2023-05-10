package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.Expression;
import com.betting.bets.rule.expression.HandicapExpression;
import com.betting.bets.stake.StakeOutcome;

public class SecondHandicapReturn implements Rule {
    @Override
    public boolean evaluate(Expression expression) {
        HandicapExpression handicapExpression = (HandicapExpression) expression;
        return handicapExpression.getHandicapPrefix().equals("Фора2")
                && handicapExpression.getPointDifference() == handicapExpression.getHandicapValue();
    }

    @Override
    public StakeOutcome getOutcome() {
        return StakeOutcome.RETURN;
    }
}
