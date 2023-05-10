package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.BothScoreExpression;
import com.betting.results.ResultPair;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BothDoScoreWinRuleTest extends RuleTest {
    @Mock
    private BothScoreExpression expression;
    @TestConfiguration
    static class BothDontScoreWinRuleTestConfig {
        @Bean
        public Rule rule() {
            return new BothDoScoreWinRule();
        }
    }
    @Test
    void testEvaluateWhenBothScoredThenTrue() {
        when(expression.getBothOpponentsPoints()).thenReturn(ResultPair.of(1, 1));
        when(expression.getStakeName()).thenReturn("Yes");
        boolean result = rule.evaluate(expression);
        assertTrue(result);
    }

    @Test
    void testEvaluateWhenBothScoredThenFalse() {
        when(expression.getBothOpponentsPoints()).thenReturn(ResultPair.of(1, 1));
        when(expression.getStakeName()).thenReturn("No");
        boolean result = rule.evaluate(expression);
        assertFalse(result);
    }

}