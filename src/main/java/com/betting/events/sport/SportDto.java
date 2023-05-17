package com.betting.events.sport;

import com.betting.events.betting_entity.BettingEntity;
import lombok.Builder;

@Builder
public record SportDto(String name, boolean top, int eventsQuantity) implements BettingEntity {
}
