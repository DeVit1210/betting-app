package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.results.EventResults;
import com.betting.results.type.FightResultType;
import com.betting.results.type.Outcome;
import com.betting.results.type.ResultType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static com.betting.results.type.FightResultType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class WinnerAndFightResultTypeStakeTest extends StakeTest {
    private Stake stake;
    @Mock
    private EventResults eventResults;
    static Stream<Arguments> winArgumentsProvider() {
        return Stream.of(
                Arguments.of("W1/Submission", FIRST_FIGHTER_SUBMISSION, Outcome.FIRST_WIN),
                Arguments.of("W2/Knockout", SECOND_FIGHTER_KNOCKOUT, Outcome.SECOND_WIN),
                Arguments.of("W1/Decision", DECISION, Outcome.FIRST_WIN)
        );
    }
    static Stream<Arguments> loseArgumentsProvider() {
        return Stream.of(
                Arguments.of("W1/Knockout", DECISION, Outcome.FIRST_WIN),
                Arguments.of("W2/Decision", DECISION, Outcome.FIRST_WIN),
                Arguments.of("W1/Submission", SECOND_FIGHTER_SUBMISSION, Outcome.SECOND_WIN)
        );
    }
    void setUpTestMethod(String stakeName, FightResultType fightResultType, Outcome outcome) {
        stake = new WinnerAndFightResultTypeStake(stakeName, stakeType, ResultType.FIGHT_ENDING);
        when(eventResults.getSummary(stake.getResultType())).thenReturn(fightResultType.getIndex());
        when(eventResults.outcome()).thenReturn(outcome);
        stake.resolveOutcome(eventResults);
    }
    @ParameterizedTest
    @MethodSource("winArgumentsProvider")
    void testResolveOutcomeWin(String stakeName, FightResultType fightResultType, Outcome outcome) {
        setUpTestMethod(stakeName, fightResultType, outcome);
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @ParameterizedTest
    @MethodSource("loseArgumentsProvider")
    void testResolveOutcomeLose(String stakeName, FightResultType fightResultType, Outcome outcome) {
        setUpTestMethod(stakeName, fightResultType, outcome);
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
}