package com.betting.bets.rule.engine;

import com.betting.bets.rule.impl.*;

import java.util.List;

public class WentFullDistanceRuleEngine extends RuleEngine {
    @Override
    protected List<Rule> initRules() {
        return List.of(new WentNotFullDistanceLoseRule(), new WentNotFullDistanceWinRule(),
                new WentFullDistanceLoseRule(), new WentFullDistanceWinRule(), new Loss());
    }
}
