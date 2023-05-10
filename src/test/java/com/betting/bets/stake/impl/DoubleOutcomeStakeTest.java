package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.results.EventResults;
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
class DoubleOutcomeStakeTest extends StakeTest {
    private Stake stake;
    @Mock
    private EventResults eventResults;

    static Stream<Arguments> winArgumentsProvider() {
        return Stream.of(
                Arguments.of(FIRST_WIN.getName() + "/" + SECOND_WIN.getName(), FIRST_WIN),
                Arguments.of(FIRST_WIN.getName() + "/" + SECOND_WIN.getName(), SECOND_WIN),
                Arguments.of(FIRST_WIN.getName() + "/" + DRAW.getName(), FIRST_WIN),
                Arguments.of(FIRST_WIN.getName() + "/" + DRAW.getName(), DRAW),
                Arguments.of(SECOND_WIN.getName() + "/" + DRAW.getName(), SECOND_WIN),
                Arguments.of(SECOND_WIN.getName() + "/" + DRAW.getName(), DRAW)
        );
    }

    static Stream<Arguments> loseArgumentsProvider() {
        return Stream.of(
                Arguments.of(FIRST_WIN.getName() + "/" + SECOND_WIN.getName(), DRAW),
                Arguments.of(FIRST_WIN.getName() + "/" + DRAW.getName(), SECOND_WIN),
                Arguments.of(SECOND_WIN.getName() + "/" + DRAW.getName(), FIRST_WIN)
        );
    }

    @ParameterizedTest
    @MethodSource("winArgumentsProvider")
    void testResolveOutcomeWin(String name, Outcome outcome) {
        stake = new DoubleOutcomeStake(name, stakeType, ResultType.POINTS);
        when(eventResults.outcome()).thenReturn(outcome);
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }

    @ParameterizedTest
    @MethodSource("loseArgumentsProvider")
    void testResolveOutcomeLose(String name, Outcome outcome) {
        stake = new DoubleOutcomeStake(name, stakeType, ResultType.POINTS);
        when(eventResults.outcome()).thenReturn(outcome);
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
}