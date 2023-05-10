package com.betting.bets.rule.engine;

import com.betting.bets.rule.impl.DoNotPresentWinRule;
import com.betting.bets.rule.impl.DoPresentWinRule;
import com.betting.bets.rule.impl.Loss;
import com.betting.bets.rule.impl.Rule;

import java.util.List;

public class IsPresentRuleEngine extends RuleEngine {
    @Override
    protected List<Rule> initRules() {
        return List.of(new DoPresentWinRule(), new DoNotPresentWinRule(), new Loss());
    }
}
