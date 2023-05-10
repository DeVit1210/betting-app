package com.betting.bets.rule.engine;

import com.betting.bets.rule.expression.HandicapExpression;
import com.betting.bets.stake.StakeOutcome;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class HandicapRuleEngineTest {
    private final HandicapRuleEngine ruleEngine = new HandicapRuleEngine();
    @Mock
    private HandicapExpression expression;

    static Stream<Arguments> winArgumentsProvider() {
        return Stream.of(
                Arguments.of("Фора1", -1.5f, 2),
                Arguments.of("Фора2", -1.5f, -2),
                Arguments.of("Фора2", 0, -1)
        );
    }

    static Stream<Arguments> loseArgumentsProvider() {
        return Stream.of(
                Arguments.of("Фора1", -1.5f, 1),
                Arguments.of("Фора2", -0.5f, 0),
                Arguments.of("Фора1", 0, -1)
        );
    }

    static Stream<Arguments> returnArgumentsProvider() {
        return Stream.of(
                Arguments.of("Фора1", 0, 0),
                Arguments.of("Фора2", 0, 0),
                Arguments.of("Фора1", -1, 1),
                Arguments.of("Фора2", -1, -1)
        );
    }

    @ParameterizedTest
    @MethodSource("winArgumentsProvider")
    void testProcessWhenPlayersHandicapIsHigherThanStakeHandicap(String prefix, float handicapValue, int pointDifference) {
        when(expression.getPointDifference()).thenReturn(pointDifference);
        when(expression.getHandicapValue()).thenReturn(handicapValue);
        when(expression.getHandicapPrefix()).thenReturn(prefix);
        assertEquals(StakeOutcome.WIN, ruleEngine.process(expression));
    }

    @ParameterizedTest
    @MethodSource("loseArgumentsProvider")
    void testProcessWhenPlayersHandicapIsLowerThanStakeHandicap(String prefix, float handicapValue, int pointDifference) {
        when(expression.getPointDifference()).thenReturn(pointDifference);
        when(expression.getHandicapValue()).thenReturn(handicapValue);
        when(expression.getHandicapPrefix()).thenReturn(prefix);
        assertEquals(StakeOutcome.LOSE, ruleEngine.process(expression));
    }

    @ParameterizedTest
    @MethodSource("returnArgumentsProvider")
    void testProcessWhenPlayersHandicapIsEqualToThanStakeHandicap(String prefix, float handicapValue, int pointDifference) {
        when(expression.getPointDifference()).thenReturn(pointDifference);
        when(expression.getHandicapValue()).thenReturn(handicapValue);
        when(expression.getHandicapPrefix()).thenReturn(prefix);
        assertEquals(StakeOutcome.RETURN, ruleEngine.process(expression));
    }

}