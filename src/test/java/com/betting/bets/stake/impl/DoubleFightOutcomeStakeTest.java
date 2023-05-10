package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.StakeTest;
import com.betting.results.EventResults;
import com.betting.results.type.FightResultType;
import com.betting.results.type.ResultType;
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
class DoubleFightOutcomeStakeTest extends StakeTest {
    private Stake stake;
    @Mock
    private EventResults eventResults;
    static Stream<Arguments> winArgumentsProvider() {
        return Stream.of(
                Arguments.of("Submission/Knockout", FIRST_FIGHTER_SUBMISSION),
                Arguments.of("Submission/Knockout", SECOND_FIGHTER_KNOCKOUT),
                Arguments.of("Decision/Knockout", DECISION),
                Arguments.of("Decision/Knockout", FIRST_FIGHTER_KNOCKOUT),
                Arguments.of("Decision/Submission", FIRST_FIGHTER_SUBMISSION)
        );
    }
    static Stream<Arguments> loseArgumentsProvider() {
        return Stream.of(
                Arguments.of("Submission/Knockout", DECISION),
                Arguments.of("Decision/Knockout", SECOND_FIGHTER_SUBMISSION),
                Arguments.of("Decision/Submission", FIRST_FIGHTER_KNOCKOUT)
        );
    }

    @ParameterizedTest
    @MethodSource("winArgumentsProvider")
    void testResolveOutcomeWin(String stakeName, FightResultType fightResultType) {
        setUpTestMethod(stakeName, fightResultType);
        assertEquals(StakeOutcome.WIN, stake.getStakeOutcome());
    }
    @ParameterizedTest
    @MethodSource("loseArgumentsProvider")
    void testResolveOutcomeLose(String stakeName, FightResultType fightResultType) {
        setUpTestMethod(stakeName, fightResultType);
        assertEquals(StakeOutcome.LOSE, stake.getStakeOutcome());
    }
    private void setUpTestMethod(String stakeName, FightResultType fightResultType) {
        stake = new DoubleFightOutcomeStake(stakeName, stakeType, ResultType.FIGHT_ENDING);
        when(eventResults.getSummary(stake.getResultType())).thenReturn(fightResultType.getIndex());
        stake.resolveOutcome(eventResults);
    }
}