package com.betting.bets.stake_type;

import com.betting.events.betting_entity.BettingEntity;
import lombok.Builder;

import java.util.List;

@Builder
public record StakeTypeDto(Integer id, String name, List<String> possibleNames) implements BettingEntity { }
