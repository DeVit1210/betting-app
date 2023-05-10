package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.ResultType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PeriodDoubleStakeTest extends StakeTest {
    private Stake stake;
    @Mock
    private EventResults eventResults;
    public static Stream<Arguments> winArgumentsProvider() {
        return Stream.of(
                Arguments.of("W1/W2", ResultPair.of(1, 0)),
                Arguments.of("W1/W2", ResultPair.of(0, 1)),
                Arguments.of("W1/X", ResultPair.of(1, 0)),
                Arguments.of("W1/X", ResultPair.of(0, 0)),
                Arguments.of("X/W2", ResultPair.of(0, 1)),
                Arguments.of("X/W2", ResultPair.of(0, 0))
        );
    }
    public static Stream<Arguments> loseArgumentsProvider() {
        return Stream.of(
                Arguments.of("W1/W2", ResultPair.of(0, 0)),
                Arguments.of("W1/X", ResultPair.of(0, 1)),
                Arguments.of("X/W2", ResultPair.of(1, 0))
        );
    }
    @ParameterizedTest
    @MethodSource("winArgumentsProvider")
    void testResolveOutcomeWin(String stakeName, ResultPair periodResult) {
        stake = new PeriodDoubleStake(stakeName, stakeType, ResultType.FIRST_PERIOD_POINTS);
        when(eventResults.getBothOpponentsSummary(any(ResultType.class))).thenReturn(periodResult);
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @ParameterizedTest
    @MethodSource("loseArgumentsProvider")
    void testResolveOutcomeLose(String stakeName, ResultPair periodResult) {
        stake = new PeriodDoubleStake(stakeName, stakeType, ResultType.FIRST_PERIOD_POINTS);
        when(eventResults.getBothOpponentsSummary(any(ResultType.class))).thenReturn(periodResult);
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
}