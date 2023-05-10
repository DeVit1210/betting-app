package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.TotalExpression;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
@SpringBootTest
class TotalLowerLossRuleTest extends RuleTest {
    @Mock
    private TotalExpression expression;

    @TestConfiguration
    static class TotalLowerLossRuleTestConfig {
        @Bean
        public Rule rule() {
            return new TotalLowerLossRule();
        }
    }
    @Test
    void evaluateWhenTotalLowerAndStakeTotalHigherThanCurrent() {
        when(expression.isTotalHigher()).thenReturn(false);
        when(expression.getTotalPointsQuantity()).thenReturn(2);
        when(expression.getStakePointsQuantity()).thenReturn(2.5f);
        assertFalse(rule.evaluate(expression));
    }
    @Test
    void evaluateWhenTotalLowerAndStakeTotalEqualToCurrent() {
        when(expression.isTotalHigher()).thenReturn(false);
        when(expression.getTotalPointsQuantity()).thenReturn(2);
        when(expression.getStakePointsQuantity()).thenReturn(2.0f);
        assertFalse(rule.evaluate(expression));
    }
    @Test
    void evaluateWhenTotalLowerAndStakeTotalLowerThanCurrent() {
        when(expression.isTotalHigher()).thenReturn(false);
        when(expression.getTotalPointsQuantity()).thenReturn(2);
        when(expression.getStakePointsQuantity()).thenReturn(1.5f);
        assertTrue(rule.evaluate(expression));
    }
    @Test
    void evaluateWhenTotalHigher() {
        when(expression.isTotalHigher()).thenReturn(true);
        assertFalse(rule.evaluate(expression));
    }

}