package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.Expression;
import com.betting.bets.rule.expression.HandicapExpression;
import com.betting.bets.stake.StakeOutcome;

public class FirstHandicapReturn implements Rule {
    @Override
    public boolean evaluate(Expression expression) {
        HandicapExpression handicapExpression = (HandicapExpression) expression;
        return handicapExpression.getHandicapPrefix().equals("Фора1")
                && handicapExpression.getPointDifference() == -1 * handicapExpression.getHandicapValue();
    }

    @Override
    public StakeOutcome getOutcome() {
        return StakeOutcome.RETURN;
    }
}