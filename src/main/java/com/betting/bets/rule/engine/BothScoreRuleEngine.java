package com.betting.bets.rule.engine;

import com.betting.bets.rule.impl.*;

import java.util.List;

public class BothScoreRuleEngine extends RuleEngine{
    @Override
    protected List<Rule> initRules() {
        return List.of(new BothDoScoreWinRule(), new BothDontScoreWinRule(),
                new BothDontScoreLoseRule(), new BothDoScoreLoseRule());
    }
}
