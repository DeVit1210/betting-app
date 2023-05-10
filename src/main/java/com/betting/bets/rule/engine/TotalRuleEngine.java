package com.betting.bets.rule.engine;

import com.betting.bets.rule.impl.*;

import java.util.List;

public class TotalRuleEngine extends RuleEngine {
    @Override
    protected List<Rule> initRules() {
        return List.of(new TotalLowerLossRule(), new TotalLowerWonRule(),
                new TotalHigherWonRule(), new TotalHigherLossRule(), new TotalReturnRule());
    }
}
