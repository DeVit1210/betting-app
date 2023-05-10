package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.Expression;
import com.betting.bets.rule.expression.OutcomeExpression;
import com.betting.bets.stake.StakeOutcome;
import com.betting.results.type.Outcome;

public class DoubleOutcomeFirstWinRule implements Rule {
    @Override
    public boolean evaluate(Expression expression) {
        OutcomeExpression outcomeExpression = (OutcomeExpression) expression;
        return outcomeExpression.getStakeWinner().contains(Outcome.FIRST_WIN.getName()) &&
                outcomeExpression.getCurrentOutcome().equals(Outcome.FIRST_WIN);
    }
    @Override
    public StakeOutcome getOutcome() {
        return StakeOutcome.WIN;
    }
}
