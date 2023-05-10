package com.betting.bets.stake_type;

import com.betting.bets.stake_type.impl.MultipleStakeType;
import com.betting.bets.stake_type.impl.OutcomeStakeType;
import com.betting.bets.stake_type.impl.YesNoStakeType;
import org.springframework.stereotype.Component;

@Component
public class StakeTypeFactory {
    public final String YES_NO_STAKE_TYPE = "yes/no";
    public final String MULTIPLE_CHOICE_STAKE_TYPE = "multiple choice";
    public final String OUTCOME_STAKE_TYPE = "outcome";
    public StakeType createStakeType(String stakeTypeName, StakeTypeSpec stakeTypeSpec) {
        return switch (stakeTypeName) {
            case YES_NO_STAKE_TYPE -> new YesNoStakeType(stakeTypeSpec);
            case MULTIPLE_CHOICE_STAKE_TYPE -> new MultipleStakeType(stakeTypeSpec);
            case OUTCOME_STAKE_TYPE -> new OutcomeStakeType(stakeTypeSpec);
            default -> throw new IllegalArgumentException("illegal stake type name");
        };
    }
}
