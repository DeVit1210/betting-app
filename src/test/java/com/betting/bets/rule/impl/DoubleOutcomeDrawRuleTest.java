package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.OutcomeExpression;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static com.betting.results.type.Outcome.DRAW;
import static com.betting.results.type.Outcome.getAnyBut;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class DoubleOutcomeDrawRuleTest extends RuleTest {
    @Mock
    private OutcomeExpression expression;
    @TestConfiguration
    static class DoubleOutcomeDrawRuleTestConfig {
        @Bean
        public Rule rule() {
            return new DoubleOutcomeDrawRule();
        }
    }

    @Test
    void testEvaluateWhenResultIsDrawAndStakeContainsDraw() {
        when(expression.getCurrentOutcome()).thenReturn(DRAW);
        when(expression.getStakeWinner()).thenReturn(DRAW.getName());
        assertTrue(rule.evaluate(expression));
    }

    @Test
    void testEvaluateWhenResultIsDrawAndStakeDoesntContainDraw() {
        when(expression.getCurrentOutcome()).thenReturn(DRAW);
        when(expression.getStakeWinner()).thenReturn(getAnyBut(DRAW).getName());
        assertFalse(rule.evaluate(expression));
    }

    @Test
    void testEvaluateWhenResultIsNotDrawAndStakeContainsDraw() {
        when(expression.getCurrentOutcome()).thenReturn(getAnyBut(DRAW));
        when(expression.getStakeWinner()).thenReturn(DRAW.getName());
        assertFalse(rule.evaluate(expression));
    }

    @Test
    void testEvaluateWhenResultIsNotDrawAndStakeDoesntContainDraw() {
        when(expression.getCurrentOutcome()).thenReturn(getAnyBut(DRAW));
        when(expression.getStakeWinner()).thenReturn(getAnyBut(DRAW).getName());
        assertFalse(rule.evaluate(expression));
    }

}