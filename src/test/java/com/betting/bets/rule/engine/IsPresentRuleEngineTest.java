package com.betting.bets.rule.engine;

import com.betting.bets.rule.expression.IsPresentExpression;
import com.betting.bets.stake.StakeOutcome;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class IsPresentRuleEngineTest {
    private final IsPresentRuleEngine ruleEngine = new IsPresentRuleEngine();
    @Mock
    private IsPresentExpression expression;

    @Test
    void testProcessWhenSomethingHappenAndPlayerBetOnIt() {
        when(expression.getTotalSummary()).thenReturn(1);
        when(expression.getStakeName()).thenReturn("Yes");
        assertEquals(StakeOutcome.WIN, ruleEngine.process(expression));
    }

    @Test
    void testProcessWhenSomethingHappenAndPlayerBetAgainstIt() {
        when(expression.getTotalSummary()).thenReturn(1);
        when(expression.getStakeName()).thenReturn("No");
        assertEquals(StakeOutcome.LOSE, ruleEngine.process(expression));
    }

    @Test
    void testProcessWhenSomethingDidntHappenAndPlayerBetOnIt() {
        when(expression.getTotalSummary()).thenReturn(0);
        when(expression.getStakeName()).thenReturn("No");
        assertEquals(StakeOutcome.WIN, ruleEngine.process(expression));
    }

    @Test
    void testProcessWhenSomethingDidntHappenAndPlayerBetAgainstIt() {
        when(expression.getTotalSummary()).thenReturn(0);
        when(expression.getStakeName()).thenReturn("Yes");
        assertEquals(StakeOutcome.LOSE, ruleEngine.process(expression));
    }

}