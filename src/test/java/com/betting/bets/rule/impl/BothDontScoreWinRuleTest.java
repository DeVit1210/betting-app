package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.BothScoreExpression;
import com.betting.results.ResultPair;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class BothDontScoreWinRuleTest extends RuleTest {
    @Mock
    private BothScoreExpression expression;
    @TestConfiguration
    static class BothDontScoreWinRuleTestConfig {
        @Bean
        public Rule rule() {
            return new BothDontScoreWinRule();
        }
    }
    @Test
    void testEvaluateWhenAtLeastOneScored() {
        when(expression.getBothOpponentsPoints()).thenReturn(ResultPair.of(1, 0));
        when(expression.getStakeName()).thenReturn("No");
        boolean result = rule.evaluate(expression);
        assertTrue(result);
    }

    @Test
    void testEvaluateWhenBothScored() {
        when(expression.getBothOpponentsPoints()).thenReturn(ResultPair.of(1, 1));
        when(expression.getStakeName()).thenReturn("No");
        boolean result = rule.evaluate(expression);
        assertFalse(result);
    }
}