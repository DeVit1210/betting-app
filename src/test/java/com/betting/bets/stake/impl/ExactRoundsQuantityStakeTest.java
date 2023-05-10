package com.betting.bets.stake.impl;

import com.betting.bets.rule.engine.RuleEngine;
import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.results.EventResults;
import com.betting.results.type.ResultType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ExactRoundsQuantityStakeTest extends StakeTest {
    private Stake stake;
    @Mock
    private EventResults eventResults;
    @ParameterizedTest
    @ValueSource(ints = {1,3,5})
    void testResolveOutcomeSuccess(int roundsQuantity) {
        stake = new ExactRoundsQuantityStake(String.valueOf(roundsQuantity), stakeType, ResultType.ROUNDS);
        when(eventResults.getSummary(any(ResultType.class))).thenReturn(roundsQuantity);
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @ParameterizedTest
    @ValueSource(ints = {1,3,5})
    void testResolveOutcomeFail(int roundsQuantity) {
        stake = new ExactRoundsQuantityStake(String.valueOf(roundsQuantity), stakeType, ResultType.ROUNDS);
        when(eventResults.getSummary(any(ResultType.class))).thenReturn(roundsQuantity-1);
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
}