package com.betting.bets.rule.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HandicapExpression implements Expression {
    private String handicapPrefix;
    private float handicapValue;
    private int pointDifference;
}
