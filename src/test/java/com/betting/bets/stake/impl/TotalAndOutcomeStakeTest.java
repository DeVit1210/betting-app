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
import static org.mockito.Mockito.when;

@SpringBootTest
class TotalAndOutcomeStakeTest extends StakeTest {
    private Stake stake;
    @Mock
    private EventResults eventResults;
    static Stream<Arguments> winArgumentsProvider() {
        return Stream.of(
                Arguments.of("W1/ТБ 2.5", ResultPair.of(2, 1), FIRST_WIN),
                Arguments.of("W2/ТБ 2.5", ResultPair.of(1, 2), SECOND_WIN),
                Arguments.of("X/ТБ 2.5", ResultPair.of(2, 2), DRAW),
                Arguments.of("W1/TМ 2.5", ResultPair.of(1, 0), FIRST_WIN),
                Arguments.of("W2/TМ 2.5", ResultPair.of(0, 1), SECOND_WIN),
                Arguments.of("X/TМ 2.5", ResultPair.of(1, 1), DRAW)
        );
    }
    static Stream<Arguments> loseArgumentsProvider() {
        return Stream.of(
                Arguments.of("W1/ТБ 2.5", ResultPair.of(1, 1), getAnyBut(FIRST_WIN)),
                Arguments.of("W2/ТБ 2.5", ResultPair.of(1, 1), getAnyBut(SECOND_WIN)),
                Arguments.of("X/ТБ 2.5", ResultPair.of(2, 1), getAnyBut(DRAW)),
                Arguments.of("W1/TМ 2.5", ResultPair.of(2, 1), FIRST_WIN),
                Arguments.of("W2/TМ 2.5", ResultPair.of(0, 3), SECOND_WIN),
                Arguments.of("X/TМ 2.5", ResultPair.of(2, 2), DRAW)
        );
    }
    private void setUpTestMethod(String stakeName, ResultPair pointsQuantity, Outcome outcome) {
        stake = new TotalAndOutcomeStake(stakeName, stakeType, ResultType.POINTS);
        when(eventResults.getOpponentsPoints()).thenReturn(pointsQuantity);
        when(eventResults.outcome()).thenReturn(outcome);
        stake.resolveOutcome(eventResults);
    }
    @ParameterizedTest
    @MethodSource("winArgumentsProvider")
    void testResolveOutcomeWin(String stakeName, ResultPair pointsQuantity, Outcome outcome) {
        setUpTestMethod(stakeName, pointsQuantity, outcome);
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @ParameterizedTest
    @MethodSource("loseArgumentsProvider")
    void testResolveOutcomeLose(String stakeName, ResultPair pointsQuantity, Outcome outcome) {
        setUpTestMethod(stakeName, pointsQuantity, outcome);
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
}