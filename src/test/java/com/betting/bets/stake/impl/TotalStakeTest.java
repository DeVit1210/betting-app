package com.betting.bets.stake.impl;

import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.results.type.ResultType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class TotalStakeTest extends StakeTest {
    private TotalStake stake;

    static Stream<Arguments> winArgumentsProvider() {
        return Stream.of(
                Arguments.of("ТБ 3.5", 4),
                Arguments.of("ТМ 3.5", 3)
        );
    }
    static Stream<Arguments> loseArgumentsProvider() {
        return Stream.of(
                Arguments.of("ТБ 3.5", 3),
                Arguments.of("ТМ 3.5", 4)
        );
    }
    static Stream<Arguments> returnArgumentsProvider() {
        return Stream.of(
                Arguments.of("ТБ 3", 3),
                Arguments.of("ТМ 3", 3)
        );
    }
    @ParameterizedTest
    @MethodSource("winArgumentsProvider")
    void testResolveTotalOutcomeWin(String stakeName, int pointsQuantity) {
        stake = new BothOpponentsTotalStake(stakeName, stakeType, ResultType.POINTS);
        StakeOutcome result = stake.resolveTotalOutcome(pointsQuantity);
        assertEquals(StakeOutcome.WIN, result);
    }
    @ParameterizedTest
    @MethodSource("loseArgumentsProvider")
    void testResolveTotalOutcomeLose(String stakeName, int pointsQuantity) {
        stake = new BothOpponentsTotalStake(stakeName, stakeType, ResultType.POINTS);
        StakeOutcome result = stake.resolveTotalOutcome(pointsQuantity);
        assertEquals(StakeOutcome.LOSE, result);
    }
    @ParameterizedTest
    @MethodSource("returnArgumentsProvider")
    void testResolveTotalOutcomeReturn(String stakeName, int pointsQuantity) {
        stake = new BothOpponentsTotalStake(stakeName, stakeType, ResultType.POINTS);
        StakeOutcome result = stake.resolveTotalOutcome(pointsQuantity);
        assertEquals(StakeOutcome.RETURN, result);
    }
}