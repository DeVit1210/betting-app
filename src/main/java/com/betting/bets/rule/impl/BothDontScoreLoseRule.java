package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.BothScoreExpression;
import com.betting.bets.rule.expression.Expression;
import com.betting.bets.stake.StakeOutcome;

public class BothDontScoreLoseRule implements Rule {
    @Override
    public boolean evaluate(Expression expression) {
        BothScoreExpression bothScoreExpression = (BothScoreExpression) expression;
        return (bothScoreExpression.getBothOpponentsPoints().getFirstOpponentResult() == 0
                || bothScoreExpression.getBothOpponentsPoints().getSecondOpponentResult() == 0)
                && bothScoreExpression.getStakeName().contains("Yes");
    }
    @Override
    public StakeOutcome getOutcome() {
        return StakeOutcome.LOSE;
    }
}
