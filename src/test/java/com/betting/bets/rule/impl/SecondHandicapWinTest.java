package com.betting.bets.rule.impl;

import com.betting.bets.rule.expression.HandicapExpression;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class SecondHandicapWinTest extends RuleTest {
    @Mock
    private HandicapExpression expression;
    @TestConfiguration
    static class SecondHandicapWinTestConfig {
        @Bean
        public Rule rule() {
            return new SecondHandicapWin();
        }
    }

    @Test
    void evaluateWhenFirstHasHandicapEqualToDifference() {
        when(expression.getHandicapPrefix()).thenReturn("Фора2");
        when(expression.getHandicapValue()).thenReturn(-2.0f);
        when(expression.getPointDifference()).thenReturn(-2);
        assertFalse(rule.evaluate(expression));
    }

    @Test
    void evaluateWhenFirstHasHandicapLessThatDifference() {
        when(expression.getHandicapPrefix()).thenReturn("Фора2");
        when(expression.getHandicapValue()).thenReturn(-1.5f);
        when(expression.getPointDifference()).thenReturn(-2);
        assertTrue(rule.evaluate(expression));
    }

    @Test
    void evaluateWhenFirstHasHandicapHigherThatDifference() {
        when(expression.getHandicapPrefix()).thenReturn("Фора2");
        when(expression.getHandicapValue()).thenReturn(-2.5f);
        when(expression.getPointDifference()).thenReturn(-2);
        assertFalse(rule.evaluate(expression));
    }
}