package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.TotalExpression;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TotalHigherWonRuleTest extends RuleTest {
    @Mock
    private TotalExpression expression;
    @TestConfiguration
    static class TotalHigherWonRuleTestConfig {
        @Bean
        public Rule rule() {
            return new TotalHigherWonRule();
        }
    }
    @Test
    void evaluateWhenTotalHigherAndStakeTotalLowerThatCurrent() {
        when(expression.isTotalHigher()).thenReturn(true);
        when(expression.getTotalPointsQuantity()).thenReturn(2);
        when(expression.getStakePointsQuantity()).thenReturn(1.5f);
        assertTrue(rule.evaluate(expression));
    }
    @Test
    void evaluateWhenTotalHigherAndStakeTotalEqualToCurrent() {
        when(expression.isTotalHigher()).thenReturn(true);
        when(expression.getTotalPointsQuantity()).thenReturn(2);
        when(expression.getStakePointsQuantity()).thenReturn(2.0f);
        assertFalse(rule.evaluate(expression));
    }
    @Test
    void evaluateWhenTotalLower() {
        when(expression.isTotalHigher()).thenReturn(false);
        assertFalse(rule.evaluate(expression));
    }
}