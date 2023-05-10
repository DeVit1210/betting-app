package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.OutcomeExpression;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static com.betting.results.type.Outcome.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class SecondWinRuleTest extends RuleTest {
    @Mock
    private OutcomeExpression expression;
    @TestConfiguration
    static class SecondWinRuleTestConfig {
        @Bean
        public Rule rule() {
            return new SecondWinRule();
        }
    }
    @Test
    void evaluateWhenTheResultIsSecondWinAndStakeIsSecondWin() {
        when(expression.getCurrentOutcome()).thenReturn(SECOND_WIN);
        when(expression.getStakeWinner()).thenReturn(SECOND_WIN.getName());
        assertTrue(rule.evaluate(expression));
    }
    @Test
    void evaluateWhenTheResultIsSecondWinAndStakeIsNotSecondWin() {
        when(expression.getCurrentOutcome()).thenReturn(SECOND_WIN);
        when(expression.getStakeWinner()).thenReturn(getAnyBut(SECOND_WIN).getName());
        assertFalse(rule.evaluate(expression));
    }
    @Test
    void evaluateWhenTheResultIsNotSecondWinAndStakeIsSecondWin() {
        when(expression.getCurrentOutcome()).thenReturn(getAnyBut(SECOND_WIN));
        when(expression.getStakeWinner()).thenReturn(SECOND_WIN.getName());
        assertFalse(rule.evaluate(expression));
    }

}