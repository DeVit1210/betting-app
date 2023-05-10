package com.betting.bets.stake_type.impl;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake_type.StakeType;
import com.betting.bets.stake_type.StakeTypeSpec;
import com.betting.events.sport.Sport;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class MultipleStakeType extends StakeType {
    public MultipleStakeType(String name, List<Stake> stakes,
                             List<Sport> sports, String className, String resultTypeName,
                             List<String> possibleBetNames) {
        super(name, stakes, sports, className, resultTypeName, possibleBetNames);
    }

    public MultipleStakeType(StakeTypeSpec stakeTypeSpec) {
        super(stakeTypeSpec);
    }

    @Override
    public void setPossibleNames(List<String> possibleBetNames) {
        this.possibleBetNames = possibleBetNames;
    }
}
