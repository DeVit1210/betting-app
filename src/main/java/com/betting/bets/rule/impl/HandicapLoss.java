package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.Expression;
import com.betting.bets.stake.StakeOutcome;

public class HandicapLoss implements Rule {
    @Override
    public boolean evaluate(Expression expression) {
        return true;
    }

    @Override
    public StakeOutcome getOutcome() {
        return StakeOutcome.LOSE;
    }
}
