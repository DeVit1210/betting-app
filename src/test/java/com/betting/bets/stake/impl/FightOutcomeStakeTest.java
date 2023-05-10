package com.betting.bets.stake.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeTest;
import com.betting.bets.stake_type.StakeType;
import com.betting.results.EventResults;
import com.betting.results.ResultPair;
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
@SpringBootTest
class FightOutcomeStakeTest extends StakeTest {
    private FightOutcomeStake stake;
    @Mock
    private EventResults eventResults;
//    static Stream<Arguments> loseArgumentsProvider() {
//        return Stream.of(
//                Arguments.of(FIRST_FIGHTER_KNOCKOUT, SECOND_FIGHTER_KNOCKOUT),
//                Arguments.of(DECISION, NO_CONTEST),
//                Arguments.of(FIRST_FIGHTER_DISQUALIFICATION, FIRST_FIGHTER_KNOCKOUT)
//        );
//    }
    static Stream<Arguments> drawArgumentsProvider() {
        return Stream.of(
                Arguments.of(ResultPair.of(1,1)),
                Arguments.of(ResultPair.of(1,0)),
                Arguments.of(ResultPair.of(0,1))
        );
    }


    @Test
    void testResolveOutcomeFirstWin() {
        stake = new DoubleFightOutcomeStake(DECISION.getStakeOutcomeName(), stakeType, ResultType.FIGHT_ENDING);
        assertEquals(Outcome.FIRST_WIN, stake.getOutcomeByResultPair(ResultPair.of(2,1)));
    }
    @Test
    void testResolveOutcomeSecondWin() {
        stake = new DoubleFightOutcomeStake(DECISION.getStakeOutcomeName(), stakeType, ResultType.FIGHT_ENDING);
        assertEquals(Outcome.SECOND_WIN, stake.getOutcomeByResultPair(ResultPair.of(1,2)));
    }
    @ParameterizedTest
    @MethodSource("drawArgumentsProvider")
    void testResolveOutcomeDraw(ResultPair results) {
        stake = new DoubleFightOutcomeStake(DECISION.getStakeOutcomeName(), stakeType, ResultType.FIGHT_ENDING);
        assertEquals(Outcome.DRAW, stake.getOutcomeByResultPair(results));
    }

}