package com.betting.bets.stake.impl;

import com.betting.bets.rule.engine.BothScoreRuleEngine;
import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.bets.stake_type.StakeType;
import com.betting.bets.stake_type.impl.MultipleStakeType;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.ResultType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class BothScoreAndTotalScoreTest extends StakeTest {
    private TotalStake stake;
    @Mock
    private EventResults eventResults;
    private void setUpTestMethod(String stakeName, ResultPair pointsQuantity) {
        stake = new BothScoreAndTotalScore(stakeName, stakeType, ResultType.POINTS);
        when(eventResults.getOpponentsPoints()).thenReturn(pointsQuantity);
    }
    static Stream<Arguments> winArgumentsProvider() {
        return Stream.of(
                Arguments.of("No/ТБ 2.5", ResultPair.of(3, 0)),
                Arguments.of("Yes/ТБ 2.5", ResultPair.of(2, 1)),
                Arguments.of("Yes/ТМ 2.5", ResultPair.of(1, 1)),
                Arguments.of("No/ТМ 2.5", ResultPair.of(1, 0))
        );
    }
    static Stream<Arguments> loseArgumentsProvider() {
        return Stream.of(
                Arguments.of("No/ТБ 2.5", ResultPair.of(2, 0)),
                Arguments.of("Yes/ТБ 2.5", ResultPair.of(1, 1)),
                Arguments.of("Yes/ТМ 2.5", ResultPair.of(0, 2)),
                Arguments.of("No/ТМ 2.5", ResultPair.of(3, 0))
        );
    }
    @ParameterizedTest
    @MethodSource("winArgumentsProvider")
    void testResolveOutcomeWin(String stakeName, ResultPair pointsQuantity) {
        setUpTestMethod(stakeName, pointsQuantity);
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @ParameterizedTest
    @MethodSource("loseArgumentsProvider")
    void testResolveOutcomeLose(String stakeName, ResultPair pointsQuantity) {
        setUpTestMethod(stakeName, pointsQuantity);
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
}