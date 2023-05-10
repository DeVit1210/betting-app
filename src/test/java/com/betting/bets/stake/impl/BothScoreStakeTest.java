package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
import com.betting.results.type.ResultType;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class BothScoreStakeTest extends StakeTest {
    private Stake stake;
    @Mock
    private EventResults eventResults;

    private void setUpTestMethod(String stakeName, ResultPair currentResult) {
        stake = new BothScoreStake(stakeName, stakeType, ResultType.POINTS);
        when(eventResults.getOpponentsPoints()).thenReturn(currentResult);
        stake.resolveOutcome(eventResults);
    }
    @Test
    void testResolveOutcomeWhenBothScoredAndPlayerBetOnIt() {
        setUpTestMethod("Yes", ResultPair.of(1,1));
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }

    @Test
    void testResolveOutcomeWhenBothScoredAndPlayerBetAgainstIt() {
        setUpTestMethod("No", ResultPair.of(1,1));
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
    @Test
    void testResolveOutcomeWhenBothDidntScoreAndPlayerBetOnIt() {
        setUpTestMethod("No", ResultPair.of(1,0));
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @Test
    void testResolveOutcomeWhenBothDidntScoreAndPlayerBetAgainstIt() {
        setUpTestMethod("Yes", ResultPair.of(1,0));
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
}
