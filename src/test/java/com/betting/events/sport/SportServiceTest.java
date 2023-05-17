package com.betting.events.sport;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.country.Country;
import com.betting.events.event.Event;
import com.betting.events.tournament.Tournament;
import com.betting.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class SportServiceTest {
    @InjectMocks
    private SportService sportService;
    @Mock
    private SportRepository sportRepository;
    @Value("${test.exception.sport-not-found}")
    private String exceptionMessage;
    @Test
    void testGetSportSuccess() {
        Sport sport = mock(Sport.class);
        when(sportRepository.findById(anyInt())).thenReturn(Optional.of(sport));
        Sport foundSport = sportService.getSport(1);
        assertNotNull(foundSport);
        verify(sportRepository, times(1)).findById(1);
    }

    @Test
    void testGetSportNotFound() {
        Sport sport = mock(Sport.class);
        when(sportRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> sportService.getSport(1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(exceptionMessage);
    }

    @Test
    void testGetTopSportsSuccess() {
        List<Sport> sports = new ArrayList<>();
        IntStream.range(0, 5).forEach(value -> sports.add(mock(Sport.class)));
        when(sportRepository.findAllSportByTopIsTrueOrderByNameAsc()).thenReturn(Streamable.of(sports));
        BettingResponse response = sportService.getTopSports();
        assertNotNull(response.getEntities());
        assertEquals(response.getEntities().size(), 5);
    }

    @Test
    void testGetSportEventsQuantity() {
        List<Event> events = new ArrayList<>();
        IntStream.range(0,5).forEach(value -> events.add(mock(Event.class)));
        List<Tournament> tournaments = new ArrayList<>();
        IntStream.range(0,5).forEach(value -> tournaments.add(mock(Tournament.class)));
        tournaments.forEach(tournament -> when(tournament.getEvents()).thenReturn(events));
        List<Country> countries = new ArrayList<>();
        IntStream.range(0,5).forEach(value -> countries.add(mock(Country.class)));
        countries.forEach(country -> when(country.getTournaments()).thenReturn(tournaments));
        Sport sport = mock(Sport.class);
        when(sport.getCountries()).thenReturn(countries);
        Integer quantity = sportService.getSportEventsQuantity(sport);
        assertEquals(5*5*5, quantity);
    }

    @Test
    void testGetAllSportsWithCount() {
        Sport football = mock(Sport.class);
        when(football.getEventsQuantity()).thenReturn(100);
        Sport hockey = mock(Sport.class);
        when(hockey.getEventsQuantity()).thenReturn(0);
        Sport tennis = mock(Sport.class);
        when(tennis.getEventsQuantity()).thenReturn(100);
        when(sportRepository.findAll()).thenReturn(List.of(football, hockey, tennis));
        BettingResponse response = sportService.getAllSportsWithCount();
        assertNotNull(response);
        assertEquals(response.getEntities().size(), 2);
    }

    @Test
    void testGetAllSportsWithCountEmpty() {
        List<Sport> sports = new ArrayList<>();
        IntStream.range(0,5).forEach(value -> sports.add(mock(Sport.class)));
        sports.forEach(sport -> when(sport.getEventsQuantity()).thenReturn(0));
        when(sportRepository.findAll()).thenReturn(sports);
        BettingResponse response = sportService.getAllSportsWithCount();
        assertNotNull(response);
        assertTrue(response.getEntities().isEmpty());
    }
}