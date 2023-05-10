package com.betting.bets.stake;

import com.betting.events.betting_entity.BettingEntity;
import lombok.Builder;

@Builder
public record StakeDto(String name, String stakeTypeName, float factor) implements BettingEntity {
}
