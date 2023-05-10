package com.betting.bets.rule.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TotalExpression implements Expression {
    private boolean totalHigher;
    private float stakePointsQuantity;
    private int totalPointsQuantity;
}
