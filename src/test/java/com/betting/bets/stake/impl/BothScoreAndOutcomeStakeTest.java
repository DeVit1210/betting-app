package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.ResultType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class BothScoreAndOutcomeStakeTest extends StakeTest {
    private Stake stake;
    @Mock
    private EventResults eventResults;



    static Stream<Arguments> winArgumentsProvider() {
        return Stream.of(
                Arguments.of("Yes/W1", ResultPair.of(2,1)),
                Arguments.of("Yes/X", ResultPair.of(2,2)),
                Arguments.of("No/X", ResultPair.of(0,0)),
                Arguments.of("No/W2", ResultPair.of(0,1))
        );
    }
    static Stream<Arguments> loseArgumentsProvider() {
        return Stream.of(
                Arguments.of("Yes/W1", ResultPair.of(1,1)),
                Arguments.of("Yes/W1", ResultPair.of(2,0)),
                Arguments.of("Yes/X", ResultPair.of(2,1)),
                Arguments.of("Yes/X", ResultPair.of(0,0)),
                Arguments.of("No/X", ResultPair.of(0,1)),
                Arguments.of("No/X", ResultPair.of(1,1)),
                Arguments.of("No/W2", ResultPair.of(1,2)),
                Arguments.of("No/W2", ResultPair.of(1,0))
        );
    }
    @ParameterizedTest
    @MethodSource("winArgumentsProvider")
    void testResolveOutcomeWin(String stakeName, ResultPair score) {
        stake = new BothScoreAndOutcomeStake(stakeName, stakeType, ResultType.POINTS);
        when(eventResults.getOpponentsPoints()).thenReturn(score);
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @ParameterizedTest
    @MethodSource("loseArgumentsProvider")
    void testResolveOutcomeLose(String stakeName, ResultPair score) {
        stake = new BothScoreAndOutcomeStake(stakeName, stakeType, ResultType.POINTS);
        when(eventResults.getOpponentsPoints()).thenReturn(score);
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }

}