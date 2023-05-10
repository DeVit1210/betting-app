package com.betting.test_builder.impl;

import com.betting.bets.stake.StakeDto;
import com.betting.test_builder.TestBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

@NoArgsConstructor(staticName = "aStakeDtoBuilder")
@AllArgsConstructor
@With
public class StakeDtoBuilder implements TestBuilder<StakeDto> {
    private String name = "stake";
    private float factor = 2.0f;
    private String stakeTypeName = "stakeTypeName";
    @Override
    public StakeDto build() {
        return StakeDto.builder()
                .name(name)
                .factor(factor)
                .stakeTypeName(stakeTypeName)
                .build();
    }
}
