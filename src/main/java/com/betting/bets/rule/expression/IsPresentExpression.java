package com.betting.bets.rule.expression;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IsPresentExpression implements Expression {
    private String stakeName;
    private int totalSummary;
}
