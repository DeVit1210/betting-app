package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.BothScoreExpression;
import com.betting.bets.rule.expression.IsPresentExpression;
import com.betting.results.ResultPair;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class DoNotPresentWinRuleTest extends RuleTest{
    @Mock
    private IsPresentExpression expression;
    @TestConfiguration
    static class DoNotPresentWinRuleTestConfig {
        @Bean
        public Rule rule() {
            return new DoNotPresentWinRule();
        }
    }
    @Test
    void testWhenPresentThenTrue() {
        when(expression.getTotalSummary()).thenReturn(0);
        when(expression.getStakeName()).thenReturn("No");
        assertTrue(rule.evaluate(expression));
    }

    @Test
    void testWhenPresentThenFalse() {
        when(expression.getTotalSummary()).thenReturn(0);
        when(expression.getStakeName()).thenReturn("Yes");
        assertFalse(rule.evaluate(expression));
    }

}