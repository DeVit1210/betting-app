package com.betting.bets.rule.impl;


import com.betting.bets.rule.expression.Expression;
import com.betting.bets.stake.StakeOutcome;

public interface Rule {
    boolean evaluate(Expression expression);
    StakeOutcome getOutcome();
}
