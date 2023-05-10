package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.OutcomeExpression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static com.betting.results.type.Outcome.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class DoubleOutcomeFirstWinRuleTest extends RuleTest {
    @Mock
    private OutcomeExpression expression;
    @TestConfiguration
    static class DoubleOutcomeDrawRuleTestConfig {
        @Bean
        public Rule rule() {
            return new DoubleOutcomeFirstWinRule();
        }
    }

    @Test
    void testEvaluateWhenResultIsFirstWinAndStakeContainsFirstWin() {
        when(expression.getCurrentOutcome()).thenReturn(FIRST_WIN);
        when(expression.getStakeWinner()).thenReturn(FIRST_WIN.getName());
        assertTrue(rule.evaluate(expression));
    }

    @Test
    void testEvaluateWhenResultIsFirstWinAndStakeDoesntContainFirstWin() {
        when(expression.getCurrentOutcome()).thenReturn(FIRST_WIN);
        when(expression.getStakeWinner()).thenReturn(getAnyBut(FIRST_WIN).getName());
        assertFalse(rule.evaluate(expression));
    }

    @Test
    void testEvaluateWhenResultIsNotFirstWinAndStakeContainsFirstWin() {
        when(expression.getCurrentOutcome()).thenReturn(getAnyBut(FIRST_WIN));
        when(expression.getStakeWinner()).thenReturn(FIRST_WIN.getName());
        assertFalse(rule.evaluate(expression));
    }

    @Test
    void testEvaluateWhenResultIsNotFirstWinAndStakeDoesntContainFirstWin() {
        when(expression.getCurrentOutcome()).thenReturn(getAnyBut(FIRST_WIN));
        when(expression.getStakeWinner()).thenReturn(getAnyBut(FIRST_WIN).getName());
        assertFalse(rule.evaluate(expression));
    } 
}