package com.betting.events.country;

import com.betting.events.betting_entity.BettingEntity;
import lombok.Builder;

@Builder
public record CountryDto(String name, Integer sportId) implements BettingEntity {
}
