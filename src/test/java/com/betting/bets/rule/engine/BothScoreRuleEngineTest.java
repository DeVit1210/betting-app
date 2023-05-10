package com.betting.bets.rule.engine;

import com.betting.bets.rule.expression.BothScoreExpression;
import com.betting.bets.stake.StakeOutcome;
import com.betting.results.ResultPair;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BothScoreRuleEngineTest {
    private final BothScoreRuleEngine ruleEngine = new BothScoreRuleEngine();
    @Mock
    private BothScoreExpression expression;
    @Test
    void testProcessWhenBothScoredAndPlayerBetOnIt() {
        when(expression.getStakeName()).thenReturn("Yes");
        when(expression.getBothOpponentsPoints()).thenReturn(ResultPair.of(1, 1));
        StakeOutcome result = ruleEngine.process(expression);
        assertEquals(StakeOutcome.WIN, result);
    }

    @Test
    void testProcessWhenBothScoredAndPlayerBetAgainstIt() {
        when(expression.getStakeName()).thenReturn("No");
        when(expression.getBothOpponentsPoints()).thenReturn(ResultPair.of(1, 1));
        StakeOutcome result = ruleEngine.process(expression);
        assertEquals(StakeOutcome.LOSE, result);
    }

    @Test
    void testProcessWhenBothDidntScoreAndPlayerBetOnIt() {
        when(expression.getStakeName()).thenReturn("No");
        when(expression.getBothOpponentsPoints()).thenReturn(ResultPair.of(1, 0));
        StakeOutcome result = ruleEngine.process(expression);
        assertEquals(StakeOutcome.WIN, result);
    }

    @Test
    void testProcessWhenBothDidntScoreAndPlayerBetAgainstIt() {
        when(expression.getStakeName()).thenReturn("Yes");
        when(expression.getBothOpponentsPoints()).thenReturn(ResultPair.of(1, 0));
        StakeOutcome result = ruleEngine.process(expression);
        assertEquals(StakeOutcome.LOSE, result);
    }
}