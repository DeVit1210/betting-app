package com.betting.mapping;

import com.betting.events.tournament.Tournament;
import com.betting.events.tournament.TournamentDto;
import org.springframework.stereotype.Component;

@Component
public class TournamentDtoMapper implements ObjectMapper<Tournament, TournamentDto> {
    @Override
    public TournamentDto mapFrom(Tournament tournament) {
        return TournamentDto.builder()
                .name(tournament.getTournamentName())
                .countryId(tournament.getCountry().getId())
                .build();
    }
}
