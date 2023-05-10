package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.Outcome;
import com.betting.results.type.ResultType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static com.betting.results.type.Outcome.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class BothScoreAndDoubleOutcomeStakeTest extends StakeTest {
    private Stake stake;
    @Mock
    private EventResults eventResults;
    static Stream<Arguments> winArgumentsProvider() {
        return Stream.of(
                Arguments.of("Yes/W1/W2", ResultPair.of(2,1), FIRST_WIN),
                Arguments.of("Yes/W1/X", ResultPair.of(1,1), DRAW),
                Arguments.of("Yes/X/W2", ResultPair.of(1,2), SECOND_WIN),
                Arguments.of("No/W1/W2", ResultPair.of(1,0), FIRST_WIN),
                Arguments.of("No/W1/X", ResultPair.of(0,0), DRAW),
                Arguments.of("No/X/W2", ResultPair.of(0,1), SECOND_WIN)
        );
    }
    static Stream<Arguments> loseArgumentsProvider() {
        return Stream.of(
                Arguments.of("Yes/W1/W2", ResultPair.of(1,1), DRAW),
                Arguments.of("Yes/W1/W2", ResultPair.of(1,0), FIRST_WIN),
                Arguments.of("Yes/W1/X", ResultPair.of(1,2), SECOND_WIN),
                Arguments.of("Yes/W1/X", ResultPair.of(1, 0), FIRST_WIN),
                Arguments.of("No/X/W2", ResultPair.of(1, 0), FIRST_WIN),
                Arguments.of("No/X/W2", ResultPair.of(2 ,1), FIRST_WIN)
        );
    }
    @ParameterizedTest
    @MethodSource("winArgumentsProvider")
    void testResolveOutcomeWin(String name, ResultPair pointsQuantity, Outcome outcome) {
        stake = new BothScoreAndDoubleOutcomeStake(name, stakeType, ResultType.POINTS);
        when(eventResults.getOpponentsPoints()).thenReturn(pointsQuantity);
        when(eventResults.outcome()).thenReturn(outcome);
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @ParameterizedTest
    @MethodSource("loseArgumentsProvider")
    void testResolveOutcomeLose(String name, ResultPair pointsQuantity, Outcome outcome) {
        stake = new BothScoreAndDoubleOutcomeStake(name, stakeType, ResultType.POINTS);
        when(eventResults.getOpponentsPoints()).thenReturn(pointsQuantity);
        when(eventResults.outcome()).thenReturn(outcome);
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
}