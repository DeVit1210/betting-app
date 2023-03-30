package com.betting.events.sport;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.country.Country;
import com.betting.events.event.Event;
import com.betting.events.exception.EntityNotFoundException;
import com.betting.events.tournament.Tournament;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SportService {
    private final SportRepository sportRepository;
    public BettingResponse getAllSportsWithCount() {
        List<Sport> sports = sportRepository.findAll();
        sports.forEach(sport -> sport.setEventsQuantity(getSportEventsQuantity(sport)));
        List<Sport> sportsWithAvailableEvents = sports.stream().filter(sport -> sport.getEventsQuantity() > 0).toList();
        return BettingResponse.builder().entities(sportsWithAvailableEvents).build();
    }
    public BettingResponse getTopSports() {
        return BettingResponse.builder().entities(sportRepository.findAllSportByTopIsTrueOrderByNameAsc()).build();
    }
    public Sport getSport(int id) {
        return sportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Sport.class));
    }
    public Integer getSportEventsQuantity(Sport sport) {
         return sport.getCountries().stream()
                .reduce(0, (midSum, country) ->
                    midSum + country.getTournaments().stream()
                            .reduce(0, (mid, tournament) -> mid + tournament.getEvents().size(), Integer::sum)
                , Integer::sum);
    }
}
