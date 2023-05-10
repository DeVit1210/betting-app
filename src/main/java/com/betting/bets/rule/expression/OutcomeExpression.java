package com.betting.bets.rule.expression;

import com.betting.results.type.Outcome;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OutcomeExpression implements Expression {
    private String stakeWinner;
    private Outcome currentOutcome;
}
