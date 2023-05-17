package com.betting.events.tournament;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.country.Country;
import com.betting.events.country.CountryService;
import com.betting.events.util.BettingEntityFilter;
import com.betting.events.util.ThrowableUtils;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.mapping.TournamentDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final CountryService countryService;
    private final BettingEntityFilter bettingEntityFilter;
    private final TournamentDtoMapper mapper;
    public BettingResponse getTournamentsByCountryPrematch(Integer countryId, Integer timeFilter) {
        Country country = countryService.getCountryById(countryId);
        Streamable<Tournament> allTournaments = tournamentRepository.findTournamentByCountry(country);
        ThrowableUtils.trueOrElseThrow(tournaments -> !tournaments.isEmpty(), allTournaments,
                new EntityNotFoundException("there are no available tournaments in " + country.getName() + " now"));
        List<Tournament> tournamentsWithAvailableEvents = bettingEntityFilter.filterTournaments(allTournaments, timeFilter);
        List<TournamentDto> tournamentDtoList = tournamentsWithAvailableEvents.stream().map(mapper::mapFrom).toList();
        return BettingResponse.builder().entities(tournamentDtoList).build();
    }
    public Tournament getTournamentById(Long id) {
        return tournamentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Tournament.class));
    }

    public TournamentDto addTournament(Integer countryId, String tournamentName) {
        Country country = countryService.getCountryById(countryId);
        Tournament tournament = Tournament.builder()
                .tournamentName(tournamentName)
                .country(country)
                .events(Collections.emptyList())
                .build();
        tournamentRepository.save(tournament);
        country.getTournaments().add(tournament);
        return mapper.mapFrom(tournament);
    }

    public TournamentDto deleteTournament(Long tournamentId) {
        Tournament tournament = getTournamentById(tournamentId);
        Country country = tournament.getCountry();
        // TODO: throw some custom exception
        ThrowableUtils.trueOrElseThrow(t -> t.getEvents().isEmpty(), tournament, new UnsupportedOperationException());
        country.getTournaments().remove(tournament);
        tournamentRepository.delete(tournament);
        return mapper.mapFrom(tournament);
    }
}
