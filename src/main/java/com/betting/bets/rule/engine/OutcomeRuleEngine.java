package com.betting.bets.rule.engine;

import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.rule.impl.*;

import java.util.List;

public class OutcomeRuleEngine extends RuleEngine {
    @Override
    protected List<Rule> initRules() {
        return List.of(new FirstWinRule(), new SecondWinRule(), new DrawRule(), new Loss());
    }
}
