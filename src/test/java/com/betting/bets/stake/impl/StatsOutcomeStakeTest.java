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
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class StatsOutcomeStakeTest extends StakeTest {
    private Stake stake;
    @Mock
    private EventResults eventResults;
    static Stream<Arguments> winArgumentsProvider() {
        return Stream.of(
                Arguments.of("W1", ResultType.ACES, ResultPair.of(2,1)),
                Arguments.of("W2", ResultType.BREAKS, ResultPair.of(1,2)),
                Arguments.of("X", ResultType.RED_CARDS, ResultPair.of(1, 1))
        );
    }
    static Stream<Arguments> loseArgumentsProvider() {
        return Stream.of(
                Arguments.of("W1", ResultType.ACES, ResultPair.of(0,1)),
                Arguments.of("W2", ResultType.BREAKS, ResultPair.of(1,0)),
                Arguments.of("X", ResultType.RED_CARDS, ResultPair.of(1,0))
        );
    }
    void setUpTestMethod(String stakeName, ResultType resultType, ResultPair resultPair) {
        stake = new StatsOutcomeStake(stakeName, stakeType, resultType);
        when(eventResults.getBothOpponentsSummary(resultType)).thenReturn(resultPair);
        stake.resolveOutcome(eventResults);
    }
    @ParameterizedTest
    @MethodSource("winArgumentsProvider")
    void testResolveOutcomeWin(String stakeName, ResultType resultType, ResultPair resultPair) {
        setUpTestMethod(stakeName, resultType, resultPair);
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @ParameterizedTest
    @MethodSource("loseArgumentsProvider")
    void testResolveOutcomeLose(String stakeName, ResultType resultType, ResultPair resultPair) {
        setUpTestMethod(stakeName, resultType, resultPair);
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
}