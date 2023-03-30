package com.betting.events.util;

import com.betting.events.country.Country;
import com.betting.events.event.Event;
import com.betting.events.exception.InvalidRequestParameterException;
import com.betting.events.timeFilter.TimeFilter;
import com.betting.events.timeFilter.TimeFilterRepository;
import com.betting.events.tournament.Tournament;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BettingEntityFilter {
    private final TimeFilterRepository timeFilterRepository;

    public List<Tournament> filterTournaments(Streamable<Tournament> allTournaments, int timeFilter) {
        TimeFilter filter = timeFilterRepository.findById(timeFilter)
                .orElseThrow(() -> new InvalidRequestParameterException("time filter with such id doesn't exist"));
        int hoursQuantity = filter.getHoursQuantity();
        return allTournaments
                .filter(tournament -> tournament.getEvents().size() > 0)
                .filter(tournament -> tournament.getEvents()
                        .stream()
                        .anyMatch(event -> event.getStartTime().isAfter(LocalDateTime.now()) &&
                                event.getStartTime().isBefore(LocalDateTime.now().plusHours(hoursQuantity))))
                .toList();
    }

    public List<Country> filterCountries(Streamable<Country> allCountries, int timeFilter) {
        TimeFilter filter = timeFilterRepository.findById(timeFilter)
                .orElseThrow(() -> new InvalidRequestParameterException("time filter with such id doesn't exist"));
        int hoursQuantity = filter.getHoursQuantity();
        return allCountries
                .filter(country -> country.getTournaments().stream()
                        .filter(tournament -> tournament.getEvents().size() > 0)
                        .anyMatch(tournament -> tournament.getEvents()
                                .stream()
                                .anyMatch(event -> event.getStartTime().isAfter(LocalDateTime.now()) &&
                                        event.getStartTime().isBefore(LocalDateTime.now().plusHours(hoursQuantity)))))
                .toList();
    }

    public List<Event> filterEvents(Streamable<Event> allEvents, int timeFilter) {
        TimeFilter filter = timeFilterRepository.findById(timeFilter)
                .orElseThrow(() -> new InvalidRequestParameterException("time filter with such id doesn't exist"));
        int hoursQuantity = filter.getHoursQuantity();
        return allEvents
                .filter(event -> event.getStartTime().isAfter(LocalDateTime.now()))
                .filter(event -> event.getStartTime().isBefore(LocalDateTime.now().plusHours(hoursQuantity)))
                .toList();
    }
}
