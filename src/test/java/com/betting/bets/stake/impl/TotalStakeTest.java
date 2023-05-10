package com.betting.bets.stake.impl;

import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.rule.engine.TotalRuleEngine;
import com.betting.bets.rule.expression.Expression;
import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.bets.stake_type.StakeType;
import com.betting.bets.stake_type.impl.MultipleStakeType;
import com.betting.results.type.ResultType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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