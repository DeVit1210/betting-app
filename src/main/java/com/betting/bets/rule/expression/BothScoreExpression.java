package com.betting.bets.rule.expression;

import com.betting.results.ResultPair;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BothScoreExpression implements Expression {
    private String stakeName;
    private ResultPair bothOpponentsPoints;
}
