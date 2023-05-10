package com.betting.test_builder.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.impl.ScoreStake;
import com.betting.bets.stake_type.StakeType;
import com.betting.bets.stake_type.impl.MultipleStakeType;
import com.betting.bets.stake_type.impl.OutcomeStakeType;
import com.betting.bets.stake_type.impl.YesNoStakeType;
import com.betting.events.sport.Sport;
import com.betting.results.type.ResultType;
import com.betting.test_builder.TestBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(staticName = "aStakeType")
@AllArgsConstructor
@With
public class StakeTypeBuilder implements TestBuilder<StakeType> {
    private String name = "stakeType";
    private List<Stake> stakes = Collections.emptyList();
    private List<Sport> sports = Collections.emptyList();
    private Class<?> clazz = ScoreStake.class;
    private ResultType resultType = ResultType.POINTS;
    private List<String> possibleBetNames = Collections.emptyList();

    private void setProperties(StakeType stakeType) {
        stakeType.setName(name);
        stakeType.setPossibleNames(possibleBetNames);
        stakeType.setStakes(stakes);
        stakeType.setClazz(clazz);
        stakeType.setResultType(resultType);
        stakeType.setSports(sports);
    }

    @Override
    public StakeType build() {
        StakeType stakeType = new MultipleStakeType();
        setProperties(stakeType);
        return stakeType;
    }

    public StakeType buildYesNoStakeType() {
        StakeType stakeType = new YesNoStakeType();
        setProperties(stakeType);
        return stakeType;
    }

    public StakeType buildOutcomeStakeType() {
        StakeType stakeType = new OutcomeStakeType();
        setProperties(stakeType);
        return stakeType;
    }
}


