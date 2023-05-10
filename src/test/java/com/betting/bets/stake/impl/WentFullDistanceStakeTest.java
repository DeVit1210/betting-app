package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.results.EventResults;
import com.betting.results.type.FightResultType;
import com.betting.results.type.ResultType;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static com.betting.results.type.FightResultType.DECISION;
import static com.betting.results.type.FightResultType.getAnyBut;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class WentFullDistanceStakeTest extends StakeTest {
    private Stake stake;
    @Mock
    private EventResults eventResults;
    private void setUpTestMethod(String stakeName, FightResultType fightResultType) {
        stake = new WentFullDistanceStake(stakeName, stakeType, ResultType.FIGHT_ENDING);
        when(eventResults.getSummary(stake.getResultType())).thenReturn(fightResultType.getIndex());
        stake.resolveOutcome(eventResults);
    }

    @Test
    void testResolveOutcomeWentFullDistanceAndPlayerBetOnIt() {
        setUpTestMethod("Yes", DECISION);
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @Test
    void testResolveOutcomeWentFullDistanceAndPlayerBetAgainstIt() {
        setUpTestMethod("No", DECISION);
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
    @Test
    void testResolveOutcomeDidntGoFullDistanceAndPlayerBetOnIt() {
        setUpTestMethod("No", getAnyBut(DECISION));
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @Test
    void testResolveOutcomeDidntGoFullDistanceAndPlayerBetAgainstIt() {
        setUpTestMethod("Yes", getAnyBut(DECISION));
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
}