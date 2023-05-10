package com.betting.bets.stake_type.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake_type.StakeType;
import com.betting.bets.stake_type.StakeTypeSpec;
import com.betting.events.sport.Sport;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.betting.results.type.Outcome.*;
@Entity
@NoArgsConstructor
public class OutcomeStakeType extends StakeType {
    public OutcomeStakeType(String name, List<Stake> stakes,
                            List<Sport> sports, String className, String resultTypeName,
                            List<String> possibleBetNames) {
        super(name, stakes, sports, className, resultTypeName, possibleBetNames);
    }

    public OutcomeStakeType(StakeTypeSpec stakeTypeSpec) {
        super(stakeTypeSpec);
    }

    @Override
    public void setPossibleNames(List<String> possibleBetNames) {
        this.possibleBetNames = List.of(FIRST_WIN.getName(), SECOND_WIN.getName(), DRAW.getName());
    }
}
