package com.betting.bets.stake.impl;

import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.ResultType;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class BothOpponentsTotalStakeTest extends StakeTest {
    private TotalStake stake;
    @Mock
    private EventResults eventResults;
    @Test
    void testResolveOutcomeWin() {
        stake = new BothOpponentsTotalStake("ТБ 1.5", stakeType, ResultType.POINTS);
        when(eventResults.getBothOpponentsSummary(any(ResultType.class))).thenReturn(ResultPair.of(1, 1));
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @Test
    void testResolveOutcomeLose() {
        stake = new BothOpponentsTotalStake("ТМ 1.5", stakeType, ResultType.POINTS);
        when(eventResults.getBothOpponentsSummary(any(ResultType.class))).thenReturn(ResultPair.of(1, 1));
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
    @Test
    void testResolveOutcomeReturn() {
        stake = new BothOpponentsTotalStake("ТБ 1", stakeType, ResultType.POINTS);
        when(eventResults.getBothOpponentsSummary(any(ResultType.class))).thenReturn(ResultPair.of(1, 0));
        stake.resolveOutcome(eventResults);
        assertEquals(StakeOutcome.RETURN, stake.getStakeOutcome());
    }
}