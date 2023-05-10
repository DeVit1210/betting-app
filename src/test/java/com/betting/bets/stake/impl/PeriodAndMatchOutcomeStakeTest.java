package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.Outcome;
import com.betting.results.type.ResultType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static com.betting.results.type.Outcome.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PeriodAndMatchOutcomeStakeTest extends StakeTest {
    private Stake stake;
    @Mock
    private EventResults eventResults;
    static Stream<Arguments> winArgumentsProvider() {
        return Stream.of(
                Arguments.of("W1/W1", ResultPair.of(1, 0), FIRST_WIN),
                Arguments.of("X/W1",  ResultPair.of(1, 1), FIRST_WIN),
                Arguments.of("W2/W1", ResultPair.of(0, 1), FIRST_WIN),
                Arguments.of("W1/X",  ResultPair.of(1, 0), DRAW),
                Arguments.of("W2/X",  ResultPair.of(0, 1), DRAW),
                Arguments.of("X/X",   ResultPair.of(0, 0), DRAW),
                Arguments.of("W1/W2", ResultPair.of(1, 0), SECOND_WIN),
                Arguments.of("X/W2",  ResultPair.of(0, 0), SECOND_WIN),
                Arguments.of("W2/W2", ResultPair.of(0, 1), SECOND_WIN)
        );
    }
    static Stream<Arguments> loseArgumentsProvider() {
        return Stream.of(
                Arguments.of("W1/W1", ResultPair.of(1, 0), getAnyBut(FIRST_WIN)),
                Arguments.of("W1/W1", ResultPair.of(0, 0), FIRST_WIN),
                Arguments.of("W1/X", ResultPair.of(1, 0), getAnyBut(DRAW)),
                Arguments.of("W1/X", ResultPair.of(0, 0), DRAW),
                Arguments.of("X/W2", ResultPair.of(0, 0), getAnyBut(SECOND_WIN)),
                Arguments.of("X/W2", ResultPair.of(1, 0), SECOND_WIN)
        );
    }
    private void setUpTestMethod(String stakeName, ResultPair periodResults, Outcome matchOutcome) {
        stake = new PeriodAndMatchOutcomeStake(stakeName, stakeType, ResultType.FIRST_PERIOD_POINTS);
        when(eventResults.getBothOpponentsSummary(any(ResultType.class))).thenReturn(periodResults);
        when(eventResults.outcome()).thenReturn(matchOutcome);
        stake.resolveOutcome(eventResults);
    }
    @ParameterizedTest
    @MethodSource("winArgumentsProvider")
    void testResolveOutcomeWin(String stakeName, ResultPair periodResults, Outcome matchOutcome) {
        setUpTestMethod(stakeName, periodResults, matchOutcome);
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @ParameterizedTest
    @MethodSource("loseArgumentsProvider")
    void testResolveOutcomeLose(String stakeName, ResultPair periodResults, Outcome matchOutcome) {
        setUpTestMethod(stakeName, periodResults, matchOutcome);
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
}