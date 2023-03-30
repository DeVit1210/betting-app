package com.betting.events.tournament;

import com.betting.events.betting_entity.BettingEntity;
import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.country.Country;
import com.betting.events.country.CountryService;
import com.betting.events.exception.EntityNotFoundException;
import com.betting.events.exception.InvalidRequestParameterException;
import com.betting.events.timeFilter.TimeFilter;
import com.betting.events.timeFilter.TimeFilterRepository;
import com.betting.events.util.BettingEntityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final CountryService countryService;
    private final BettingEntityFilter bettingEntityFilter;
    public BettingResponse getTournamentsByCountryPrematch(Integer countryId, Integer timeFilter) {
        Country country = countryService.getCountryById(countryId);
        Streamable<Tournament> allTournaments = tournamentRepository.findTournamentByCountry(country);
        if(allTournaments.isEmpty()) {
            throw new EntityNotFoundException("there are no available tournaments in " + country.getName() + " now");
        }
        List<Tournament> tournamentsWithAvailableEvents = bettingEntityFilter.filterTournaments(allTournaments, timeFilter);
        return BettingResponse.builder().entities(tournamentsWithAvailableEvents).build();
    }
    public Tournament getTournamentById(Long id) {
        return tournamentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Tournament.class));
    }
}
