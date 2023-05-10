package com.betting.bets.stake_type;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.impl.BothScoreStake;
import com.betting.bets.stake.impl.IsPresentStake;
import com.betting.bets.stake.impl.MatchOutcomeStake;
import com.betting.events.event.Event;
import com.betting.test_builder.impl.StakeTypeBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@SpringBootTest
class StakeTypeTest {
    @ParameterizedTest
    @ValueSource(classes = {BothScoreStake.class, MatchOutcomeStake.class, IsPresentStake.class})
    void testGenerateStakesYesNoStakeType(Class<?> clazz) throws Exception {
        StakeType stakeType = StakeTypeBuilder.aStakeType().withClazz(clazz).buildYesNoStakeType();
        Event event = mock(Event.class);
        List<Stake> result = stakeType.generateStakes(event);
        assertEquals(2, result.size());
        assertEquals(clazz, result.get(0).getClass());
    }
}