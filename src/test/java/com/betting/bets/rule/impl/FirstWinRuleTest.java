package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.OutcomeExpression;
import com.betting.results.type.Outcome;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static com.betting.results.type.Outcome.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class FirstWinRuleTest extends RuleTest {
    @Mock
    private OutcomeExpression expression;

    @TestConfiguration
    static class FirstWinRuleTestConfig {
        @Bean
        public Rule rule() {
            return new FirstWinRule();
        }
    }

    @Test
    void evaluateWhenTheResultIsFirstWinAndStakeIsFirstWin() {
        when(expression.getCurrentOutcome()).thenReturn(FIRST_WIN);
        when(expression.getStakeWinner()).thenReturn(FIRST_WIN.getName());
        assertTrue(rule.evaluate(expression));
    }
    @Test
    void evaluateWhenTheResultIsFirstWinAndStakeIsNotFirstWin() {
        when(expression.getCurrentOutcome()).thenReturn(FIRST_WIN);
        when(expression.getStakeWinner()).thenReturn(getAnyBut(FIRST_WIN).getName());
        assertFalse(rule.evaluate(expression));
    }
    @Test
    void evaluateWhenTheResultIsNotFirstWinAndStakeIsFirstWin() {
        when(expression.getCurrentOutcome()).thenReturn(getAnyBut(FIRST_WIN));
        when(expression.getStakeWinner()).thenReturn(FIRST_WIN.getName());
        assertFalse(rule.evaluate(expression));
    }

}