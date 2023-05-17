package com.betting.events.tournament;

import com.betting.events.betting_entity.BettingEntity;
import lombok.Builder;

@Builder
public record TournamentDto(String name, Integer countryId) implements BettingEntity {
}
