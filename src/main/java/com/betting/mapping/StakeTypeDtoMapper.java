package com.betting.mapping;

import com.betting.bets.stake_type.StakeType;
import com.betting.bets.stake_type.StakeTypeDto;
import org.springframework.stereotype.Component;

@Component
public class StakeTypeDtoMapper implements ObjectMapper<StakeType, StakeTypeDto> {
    @Override
    public StakeTypeDto mapFrom(StakeType stakeType) {
        return StakeTypeDto.builder()
                .name(stakeType.getName())
                .id(stakeType.getId())
                .possibleNames(stakeType.getPossibleBetNames())
                .stakes(stakeType.getStakes())
                .build();
    }
}
