package com.betting.security.auth.mapping;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeDto;

public class StakeDtoMapper implements ObjectMapper<Stake, StakeDto> {
    @Override
    public StakeDto mapFrom(Stake stake) {
        return StakeDto.builder()
                .name(stake.getName())
                .stakeTypeName(stake.getStakeType().getName())
                .factor(stake.getFactor())
                .build();
    }
}
