package com.betting.mapping;

import com.betting.bets.stake.Stake;
import com.betting.bets.stake.StakeDto;
import org.springframework.stereotype.Component;

@Component
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
