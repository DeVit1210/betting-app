package com.betting.bets.rule.engine;

import com.betting.bets.rule.expression.OutcomeExpression;
import com.betting.bets.stake.StakeOutcome;
import com.betting.results.type.Outcome;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static com.betting.results.type.Outcome.getAnyBut;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class OutcomeRuleEngineTest {
    private final OutcomeRuleEngine ruleEngine = new OutcomeRuleEngine();
    @Mock
    private OutcomeExpression expression;

    @ParameterizedTest
    @EnumSource(Outcome.class)
    void testProcessWhenTheResultEqualToPlayersBet(Outcome outcome) {
        when(expression.getCurrentOutcome()).thenReturn(outcome);
        when(expression.getStakeWinner()).thenReturn(outcome.getName());
        assertEquals(StakeOutcome.WIN, ruleEngine.process(expression));
    }
    @ParameterizedTest
    @EnumSource(Outcome.class)
    void testProcessWhenTheResultNotEqualToPlayersBet(Outcome outcome) {
        when(expression.getCurrentOutcome()).thenReturn(outcome);
        when(expression.getStakeWinner()).thenReturn(getAnyBut(outcome).getName());
        assertEquals(StakeOutcome.LOSE, ruleEngine.process(expression));
    }


}