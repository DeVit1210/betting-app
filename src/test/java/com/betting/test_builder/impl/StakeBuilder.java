package com.betting.test_builder.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeOutcome;
import com.betting.bets.stake.impl.ScoreStake;
import com.betting.bets.stake_type.StakeType;
import com.betting.bets.stake_type.impl.MultipleStakeType;
import com.betting.events.event.Event;
import com.betting.results.EventResults;
import com.betting.results.type.ResultType;
import com.betting.test_builder.TestBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

@NoArgsConstructor(staticName = "aStakeBuilder")
@AllArgsConstructor
@With
public class StakeBuilder implements TestBuilder<Stake> {
    private String name = "stake";
    private String fullStakeName = "fullStakeName";
    private float factor = 0.0f;
    private boolean isExecuted = false;
    private StakeOutcome stakeOutcome = StakeOutcome.RETURN;
    private StakeType stakeType = new MultipleStakeType();
    private ResultType resultType = ResultType.POINTS;
    private Event event = Event.builder().build();

    @Override
    public Stake build() {
        Stake stake = new ScoreStake();
        stake.setName(name);
        stake.setFullStakeName(fullStakeName);
        stake.setFactor(factor);
        stake.setExecuted(isExecuted);
        stake.setStakeOutcome(stakeOutcome);
        stake.setStakeType(stakeType);
        stake.setResultType(resultType);
        stake.setEvent(event);
        return stake;
    }
}
