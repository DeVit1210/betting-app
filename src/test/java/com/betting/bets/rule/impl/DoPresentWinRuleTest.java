package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.IsPresentExpression;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class DoPresentWinRuleTest extends RuleTest {
    @Mock
    private IsPresentExpression expression;
    @TestConfiguration
    static class DoPresentWinRuleTestConfig {
        @Bean
        public Rule rule() {
            return new DoPresentWinRule();
        }
    }
    @Test
    void testEvaluateWhenPresentThenTrue() {
        when(expression.getTotalSummary()).thenReturn(1);
        when(expression.getStakeName()).thenReturn("Yes");
        assertTrue(rule.evaluate(expression));
    }

    @Test
    void testEvaluateWhenPresentThenFalse() {
        when(expression.getTotalSummary()).thenReturn(1);
        when(expression.getStakeName()).thenReturn("No");
        assertFalse(rule.evaluate(expression));
    }
}