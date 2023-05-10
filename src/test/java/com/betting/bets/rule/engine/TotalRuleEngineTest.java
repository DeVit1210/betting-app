package com.betting.bets.rule.engine;

import com.betting.bets.rule.expression.TotalExpression;
import com.betting.bets.stake.StakeOutcome;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TotalRuleEngineTest {
    private final TotalRuleEngine ruleEngine = new TotalRuleEngine();
    @Mock
    private TotalExpression expression;

    static Stream<Arguments> winArgumentsProvider() {
        return Stream.of(
                Arguments.of(true, 1.5f, 2),
                Arguments.of(false, 1.5f, 1),
                Arguments.of(true, 0, 1)
        );
    }

    static Stream<Arguments> loseArgumentsProvider() {
        return Stream.of(
                Arguments.of(true, 1.5f, 1),
                Arguments.of(false, 1.5f, 2),
                Arguments.of(true, 0.5f, 0)
        );
    }

    static Stream<Arguments> returnArgumentsProvider() {
        return Stream.of(
                Arguments.of(true, 1, 1),
                Arguments.of(false, 1, 1)
        );
    }

    private void setUpExpressionMock(boolean isTotalHigher, float stakePointsQuantity, int totalPointsQuantity) {
        when(expression.isTotalHigher()).thenReturn(isTotalHigher);
        when(expression.getTotalPointsQuantity()).thenReturn(totalPointsQuantity);
        when(expression.getStakePointsQuantity()).thenReturn(stakePointsQuantity);
    }

    @ParameterizedTest
    @MethodSource("winArgumentsProvider")
    void testProcessPlayerWon(boolean isTotalHigher, float stakePointsQuantity, int totalPointsQuantity) {
        setUpExpressionMock(isTotalHigher, stakePointsQuantity, totalPointsQuantity);
        assertEquals(StakeOutcome.WIN, ruleEngine.process(expression));
    }
    @ParameterizedTest
    @MethodSource("loseArgumentsProvider")
    void testProcessPlayerLose(boolean isTotalHigher, float stakePointsQuantity, int totalPointsQuantity) {
        setUpExpressionMock(isTotalHigher, stakePointsQuantity, totalPointsQuantity);
        assertEquals(StakeOutcome.LOSE, ruleEngine.process(expression));
    }
    @ParameterizedTest
    @MethodSource("returnArgumentsProvider")
    void testProcessPlayerGetReturn(boolean isTotalHigher, float stakePointsQuantity, int totalPointsQuantity) {
        setUpExpressionMock(isTotalHigher, stakePointsQuantity, totalPointsQuantity);
        assertEquals(StakeOutcome.RETURN, ruleEngine.process(expression));
    }

}