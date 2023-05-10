package com.betting.bets.rule.engine;

import com.betting.bets.rule.expression.OutcomeExpression;
import com.betting.bets.stake.StakeOutcome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static com.betting.results.type.Outcome.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class DoubleOutcomeRuleEngineTest {
    private final DoubleOutcomeRuleEngine ruleEngine = new DoubleOutcomeRuleEngine();
    @Mock
    private OutcomeExpression expression;

    @ParameterizedTest
    @ValueSource(strings = {"W1/X", "W2/X"})
    void testWhenTheResultIsDrawAndPlayerBetsOnIt(String stakeName) {
        when(expression.getStakeWinner()).thenReturn(stakeName);
        when(expression.getCurrentOutcome()).thenReturn(DRAW);
        assertEquals(StakeOutcome.WIN, ruleEngine.process(expression));
    }

    @Test
    void testWhenTheResultIsDrawAndPlayerBetsAgainstIt() {
        when(expression.getStakeWinner()).thenReturn(FIRST_WIN.getName() + "/" + SECOND_WIN.getName());
        when(expression.getCurrentOutcome()).thenReturn(DRAW);
        assertEquals(StakeOutcome.LOSE, ruleEngine.process(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {"W1/W2", "W1/X"})
    void testWhenTheResultIsFirstWinAndPlayerBetsOnIt(String stakeName) {
        when(expression.getStakeWinner()).thenReturn(stakeName);
        when(expression.getCurrentOutcome()).thenReturn(FIRST_WIN);
        assertEquals(StakeOutcome.WIN, ruleEngine.process(expression));
    }

    @Test
    void testWhenTheResultIsFirstWinAndPlayerBetsAgainstIt() {
        when(expression.getStakeWinner()).thenReturn(DRAW.getName() + "/" + SECOND_WIN.getName());
        when(expression.getCurrentOutcome()).thenReturn(FIRST_WIN);
        assertEquals(StakeOutcome.LOSE, ruleEngine.process(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {"W1/W2", "W2/X"})
    void testWhenTheResultIsSecondWinAndPlayerBetsOnIt(String stakeName) {
        when(expression.getStakeWinner()).thenReturn(stakeName);
        when(expression.getCurrentOutcome()).thenReturn(SECOND_WIN);
        assertEquals(StakeOutcome.WIN, ruleEngine.process(expression));
    }

    @Test
    void testWhenTheResultIsSecondWinAndPlayerBetsAgainstIt() {
        when(expression.getStakeWinner()).thenReturn(FIRST_WIN.getName() + "/" + DRAW.getName());
        when(expression.getCurrentOutcome()).thenReturn(SECOND_WIN);
        assertEquals(StakeOutcome.LOSE, ruleEngine.process(expression));
    }

}