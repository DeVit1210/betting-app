package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.BothScoreExpression;
import com.betting.bets.rule.expression.Expression;
import com.betting.bets.stake.StakeOutcome;

public class BothDoScoreLoseRule implements Rule {
    @Override
    public boolean evaluate(Expression expression) {
        BothScoreExpression bothScoreExpression = (BothScoreExpression) expression;
        return bothScoreExpression.getBothOpponentsPoints().getFirstOpponentResult() > 0
                && bothScoreExpression.getBothOpponentsPoints().getSecondOpponentResult() > 0
                && bothScoreExpression.getStakeName().contains("No");
    }
    @Override
    public StakeOutcome getOutcome() {
        return StakeOutcome.LOSE;
    }
}
