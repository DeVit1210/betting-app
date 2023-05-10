package com.betting.bets.rule.engine;

import com.betting.bets.rule.expression.Expression;
import com.betting.bets.rule.impl.Rule;
import com.betting.bets.stake.StakeOutcome;
import org.springframework.stereotype.Component;

import java.util.List;

public abstract class RuleEngine {
    private final List<Rule> rules = initRules();
    protected abstract List<Rule> initRules();
    public StakeOutcome process(Expression expression) {
        Rule rule = rules.stream().filter(r -> r.evaluate(expression)).findFirst().orElseThrow();
        return rule.getOutcome();
    }
}
