package com.betting.events.util;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.country.Country;
import com.betting.events.event.Event;
import com.betting.events.exception.InvalidRequestParameterException;
import com.betting.events.timeFilter.TimeFilter;
import com.betting.events.timeFilter.TimeFilterRepository;
import com.betting.events.tournament.Tournament;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class BettingEntityFilterTest {
    @InjectMocks
    private BettingEntityFilter bettingEntityFilter;
    @Mock
    private TimeFilterRepository timeFilterRepository;
    private final Integer eventsQuantity = 5;
    private final Integer tournamentsQuantity = 3;
    private final Integer countriesQuantity = 2;
    private final List<Event> events = getMockList(Event.class, eventsQuantity);
    private final List<Tournament> tournaments = getMockList(Tournament.class, tournamentsQuantity);
    private final List<Country> countries = getMockList(Country.class, countriesQuantity);
    @Value("${test.exception.country-not-found}")
    private String messageException;
    @Value("${test.emptyTimeFilter}")
    private Integer emptyTimeFilter;
    @Value("${test.timeFilter}")
    private Integer nonEmptyTimeFilter;

    <T> List<T> getMockList(Class<T> classType, int listSize) {
        List<T> list = new ArrayList<>();
        IntStream.range(0,listSize).forEach(value -> list.add(mock(classType)));
        return list;
    }

    @Test
    void testFilterCountriesNoAvailableEventsEmptyTimeFilter() {
        setTimeFilterUp(true);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().minusHours(1)));
        tournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(events));
        countries.forEach(country -> when(country.getTournaments()).thenReturn(tournaments));
        List<Country> filtered = bettingEntityFilter.filterCountries(Streamable.of(countries), emptyTimeFilter);
        assertEquals(0, filtered.size());
    }
    @Test
    void testFilterCountriesSuccessEmptyTimeFilter() {
        setTimeFilterUp(true);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().plusHours(1)));
        tournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(events));
        countries.forEach(country -> when(country.getTournaments()).thenReturn(tournaments));
        List<Country> filtered = bettingEntityFilter.filterCountries(Streamable.of(countries), emptyTimeFilter);
        assertEquals(countriesQuantity, filtered.size());
    }

    @Test
    void testFilterCountriesPartlyAvailableEmptyTimeFilter() {
        setTimeFilterUp(true);
        List<Event> unavailableEvents = getMockList(Event.class, eventsQuantity);
        unavailableEvents.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().minusHours(1)));
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().plusHours(1)));
        List<Tournament> unavailableTournaments = getMockList(Tournament.class, tournamentsQuantity);
        unavailableTournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(unavailableEvents));
        tournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(events));
        when(countries.get(0).getTournaments()).thenReturn(tournaments);
        when(countries.get(1).getTournaments()).thenReturn(unavailableTournaments);
        List<Country> filtered = bettingEntityFilter.filterCountries(Streamable.of(countries), emptyTimeFilter);
        assertEquals(1, filtered.size());
    }

    @Test
    void testFilterCountriesOnlyOneEventInCountryAvailableEmptyTimeFilter() {
        setTimeFilterUp(true);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().minusHours(1)));
        when(events.get(0).getStartTime()).thenReturn(LocalDateTime.now().plusHours(1));
        tournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(events));
        countries.forEach(country -> when(country.getTournaments()).thenReturn(tournaments));
        List<Country> filtered = bettingEntityFilter.filterCountries(Streamable.of(countries), emptyTimeFilter);
        assertEquals(countriesQuantity, filtered.size());
    }

    @Test
    void testFilterCountriesSuccessWithTimeFilter() {
        setTimeFilterUp(false);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().plusHours(1)));
        tournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(events));
        countries.forEach(country -> when(country.getTournaments()).thenReturn(tournaments));
        List<Country> filtered = bettingEntityFilter.filterCountries(Streamable.of(countries), nonEmptyTimeFilter);
        assertEquals(countriesQuantity, filtered.size());
    }

    @Test
    void testGetCountriesWithAvailableEventsOnlyOneEventInCountryAvailableWithTimeFilter() {
        setTimeFilterUp(false);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().plusHours(24)));
        when(events.get(0).getStartTime()).thenReturn(LocalDateTime.now().plusHours(1));
        tournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(events));
        countries.forEach(country -> when(country.getTournaments()).thenReturn(tournaments));
        List<Country> filtered = bettingEntityFilter.filterCountries(Streamable.of(countries), nonEmptyTimeFilter);
        assertEquals(countriesQuantity, filtered.size());
    }
    @Test
    void testGetCountriesWithAvailableEventsPartlyAvailableWithTimeFilter() {
        setTimeFilterUp(false);
        List<Event> unavailableEvents = getMockList(Event.class, eventsQuantity);
        unavailableEvents.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().plusHours(24)));
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().plusHours(1)));
        List<Tournament> unavailableTournaments = getMockList(Tournament.class, tournamentsQuantity);
        unavailableTournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(unavailableEvents));
        tournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(events));
        when(countries.get(0).getTournaments()).thenReturn(tournaments);
        when(countries.get(1).getTournaments()).thenReturn(unavailableTournaments);
        List<Country> filtered = bettingEntityFilter.filterCountries(Streamable.of(countries), nonEmptyTimeFilter);
        assertEquals(1, filtered.size());
    }

    void setTimeFilterUp(boolean emptyFilter) {
        TimeFilter timeFilter = mock(TimeFilter.class);
        when(timeFilter.getHoursQuantity()).thenReturn(emptyFilter ? Integer.MAX_VALUE : 3);
        when(timeFilterRepository.findById(anyInt())).thenReturn(Optional.of(timeFilter));
    }

    @Test
    void testFilterNotFound() {
        when(timeFilterRepository.findById(anyInt()))
                .thenThrow(new InvalidRequestParameterException(messageException));
        assertThatThrownBy(() -> bettingEntityFilter.filterCountries(Streamable.of(countries), emptyTimeFilter))
                .isInstanceOf(InvalidRequestParameterException.class)
                .hasMessage(messageException);
    }

    @Test
    void testFilterTournamentsNoAvailableEventsEmptyTimeFilter() {
        setTimeFilterUp(true);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().minusHours(1)));
        tournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(events));
        List<Tournament> filtered = bettingEntityFilter.filterTournaments(Streamable.of(tournaments), emptyTimeFilter);
        assertEquals(0, filtered.size());
    }

    @Test
    void testFilterTournamentsSuccessEmptyTimeFilter() {
        setTimeFilterUp(true);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().plusHours(1)));
        tournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(events));
        List<Tournament> filtered = bettingEntityFilter.filterTournaments(Streamable.of(tournaments), emptyTimeFilter);
        assertEquals(tournamentsQuantity, filtered.size());
    }

    @Test
    void testFilterTournamentsPartlyAvailableEmptyTimeFilter() {
        setTimeFilterUp(true);
        List<Event> unavailableEvents = getMockList(Event.class, eventsQuantity);
        unavailableEvents.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().minusHours(1)));
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().plusHours(1)));
        tournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(events));
        when(tournaments.get(0).getEvents()).thenReturn(unavailableEvents);
        List<Tournament> filtered = bettingEntityFilter.filterTournaments(Streamable.of(tournaments), emptyTimeFilter);
        assertEquals(tournamentsQuantity - 1, filtered.size());
    }

    @Test
    void testFilterTournamentsSuccessWithTimeFilter() {
        setTimeFilterUp(false);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().plusHours(1)));
        tournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(events));
        List<Tournament> filtered = bettingEntityFilter.filterTournaments(Streamable.of(tournaments), emptyTimeFilter);
        assertEquals(tournamentsQuantity, filtered.size());
    }

    @Test
    void testFilterTournamentsNoAvailableEventsWithTimeFilter() {
        setTimeFilterUp(false);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().plusHours(6)));
        tournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(events));
        List<Tournament> filtered = bettingEntityFilter.filterTournaments(Streamable.of(tournaments), nonEmptyTimeFilter);
        assertEquals(0, filtered.size());
    }

    @Test
    void testFilterEventsSuccessEmptyTimeFilter() {
        setTimeFilterUp(true);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().plusHours(1)));
        List<Event> filtered = bettingEntityFilter.filterEvents(Streamable.of(events), emptyTimeFilter);
        assertEquals(eventsQuantity, filtered.size());
    }

    @Test
    void testFilterEventsNoAvailableEventsEmptyTimeFilter() {
        setTimeFilterUp(true);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().minusHours(1)));
        List<Event> filtered = bettingEntityFilter.filterEvents(Streamable.of(events), emptyTimeFilter);
        assertEquals(0, filtered.size());
    }

    @Test
    void testFilterEventsOnlyOneEventAvailableEmptyTimeFilter() {
        setTimeFilterUp(true);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().minusHours(1)));
        when(events.get(1).getStartTime()).thenReturn(LocalDateTime.now().plusHours(1));
        List<Event> filtered = bettingEntityFilter.filterEvents(Streamable.of(events), emptyTimeFilter);
        assertEquals(1, filtered.size());
    }


    @Test
    void testFilterEventsSuccessWithTimeFilter() {
        setTimeFilterUp(false);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().plusHours(1)));
        List<Event> filtered = bettingEntityFilter.filterEvents(Streamable.of(events), emptyTimeFilter);
        assertEquals(eventsQuantity, filtered.size());
    }

    @Test
    void testFilterEventsNoAvailableEventsWithTimeFilter() {
        setTimeFilterUp(false);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().plusHours(12)));
        List<Event> filtered = bettingEntityFilter.filterEvents(Streamable.of(events), emptyTimeFilter);
        assertEquals(0, filtered.size());
    }

    @Test
    void testFilterEventsOnlyOneEventAvailableWithTimeFilter() {
        setTimeFilterUp(false);
        events.forEach(event -> when(event.getStartTime()).thenReturn(LocalDateTime.now().plusHours(12)));
        when(events.get(1).getStartTime()).thenReturn(LocalDateTime.now().plusHours(1));
        List<Event> filtered = bettingEntityFilter.filterEvents(Streamable.of(events), emptyTimeFilter);
        assertEquals(1, filtered.size());
    }
}