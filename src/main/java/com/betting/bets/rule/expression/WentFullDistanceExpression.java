package com.betting.bets.rule.expression;

import com.betting.results.type.FightResultType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WentFullDistanceExpression implements Expression {
    private String stakeName;
    private FightResultType fightResultType;
}
