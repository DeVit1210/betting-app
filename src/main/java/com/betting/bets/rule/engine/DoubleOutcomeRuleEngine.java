package com.betting.bets.rule.engine;

import com.betting.bets.rule.impl.*;

import java.util.List;

public class DoubleOutcomeRuleEngine extends RuleEngine {
    @Override
    protected List<Rule> initRules() {
        return List.of(new DoubleOutcomeDrawRule(), new DoubleOutcomeFirstWinRule(),
                new DoubleOutcomeSecondWinRule(), new Loss());
    }
}
