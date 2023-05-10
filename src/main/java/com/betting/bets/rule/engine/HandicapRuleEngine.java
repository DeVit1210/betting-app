package com.betting.bets.rule.engine;

import com.betting.bets.rule.impl.*;

import java.util.List;

public class HandicapRuleEngine extends RuleEngine {
    @Override
    protected List<Rule> initRules() {
        return List.of(new FirstHandicapWin(), new FirstHandicapReturn(),
                new SecondHandicapReturn(), new SecondHandicapWin(), new HandicapLoss());
    }
}
