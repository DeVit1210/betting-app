package com.betting.bets.stake.impl;

import com.betting.bets.rule.engine.BothScoreRuleEngine;
import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.rule.engine.TotalRuleEngine;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.bets.stake_type.StakeType;
import com.betting.bets.stake_type.impl.MultipleStakeType;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.ResultType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class FirstOpponentTotalStakeTest extends StakeTest {
    private TotalStake stake;
    @Mock
    private EventResults eventResults;

    static Stream<Arguments> winArgumentsProvider() {
        return Stream.of(
                Arguments.of("ТБ 2.5", ResultType.TWO_MIN_PENALTIES, 3),
                Arguments.of("ТМ 2.5", ResultType.RED_CARDS, 2)
        );
    }
    static Stream<Arguments> loseArgumentsProvider() {
        return Stream.of(
                Arguments.of("ТБ 2.5", ResultType.TWO_MIN_PENALTIES, 2),
                Arguments.of("ТМ 2.5", ResultType.RED_CARDS, 3)
        );
    }
    @ParameterizedTest
    @MethodSource("winArgumentsProvider")
    void testResolveOutcomeWin(String name, ResultType resultType, int pointsQuantity) {
        stake = new FirstOpponentTotalStake(name, stakeType, resultType);
        when(eventResults.getSummary(any(ResultType.class))).thenReturn(pointsQuantity);
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @ParameterizedTest
    @MethodSource("loseArgumentsProvider")
    void testResolveOutcomeLose(String name, ResultType resultType, int pointsQuantity) {
        stake = new FirstOpponentTotalStake(name, stakeType, resultType);
        when(eventResults.getSummary(any(ResultType.class))).thenReturn(pointsQuantity);
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
}