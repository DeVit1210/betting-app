package com.betting.events.tournament;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.country.Country;
import com.betting.events.country.CountryService;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.events.util.BettingEntityFilter;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.test.context.TestPropertySource;

import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class TournamentServiceTest {
    @InjectMocks
    private TournamentService tournamentService;
    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private CountryService countryService;
    @Mock
    private BettingEntityFilter bettingEntityFilter;
    @Value("${test.exception.tournament-not-found}")
    private String tournamentNotFoundMessage;
    @Value("${test.exception.country-not-found}")
    private String countryNotFoundMessage;
    @Value("${test.emptyTimeFilter}")
    private Integer emptyFilter;
    @Value("${test.timeFilter}")
    private Integer nonEmptyTimeFilter;
    private final List<Tournament> tournaments = getMockList(Tournament.class, 3);

    @Test
    void testGetTournamentByIdSuccess() {
        Tournament tournament = mock(Tournament.class);
        when(tournamentRepository.findById(anyLong())).thenReturn(Optional.of(tournament));
        Tournament foundTournament = tournamentService.getTournamentById(1L);
        assertNotNull(foundTournament);
    }

    @Test
    void testGetTournamentByIdNotFound() {
        when(tournamentRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> tournamentService.getTournamentById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(tournamentNotFoundMessage);
    }
    <T> List<T> getMockList(Class<T> classType, int listSize) {
        List<T> list = new ArrayList<>();
        IntStream.range(0,listSize).forEach(value -> list.add(mock(classType)));
        return list;
    }
    @Test
    void testGetTournamentsByCountryWrongCountryId() {
        when(countryService.getCountryById(anyInt())).thenThrow(new EntityNotFoundException(countryNotFoundMessage));
        assertThatThrownBy(() -> tournamentService.getTournamentsByCountryPrematch(1, 1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(countryNotFoundMessage);
    }

    @Test
    void testGetTournamentsByCountryTournamentsNotFound() {
        Country country = mock(Country.class);
        when(countryService.getCountryById(anyInt())).thenReturn(country);
        when(tournamentRepository.findTournamentByCountry(any(Country.class))).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> tournamentService.getTournamentsByCountryPrematch(1, 1));
    }

    @Test
    void testGetTournamentsByCountrySuccessEmptyTimeFilter() {
        setUpBeforeGetTournamentsTest(true, true);
        BettingResponse response = tournamentService.getTournamentsByCountryPrematch(1, emptyFilter);
        assertNotNull(response);
    }
    @Test
    void testGetTournamentsByCountryNoAvailableTournamentsTimeFilter() {
        setUpBeforeGetTournamentsTest(false, true);
        BettingResponse response = tournamentService.getTournamentsByCountryPrematch(1, emptyFilter);
        assertNotNull(response);
    }

    @Test
    void testGetTournamentsByCountrySuccessWithTimeFilter() {
        setUpBeforeGetTournamentsTest(true, false);
        BettingResponse response = tournamentService.getTournamentsByCountryPrematch(1, nonEmptyTimeFilter);
        assertNotNull(response);
    }
    @Test
    void testGetTournamentsByCountryNoAvailableTournamentsWithTimeFilter() {
        setUpBeforeGetTournamentsTest(false, false);
        BettingResponse response = tournamentService.getTournamentsByCountryPrematch(1, nonEmptyTimeFilter);
        assertNotNull(response);
    }

    void setUpBeforeGetTournamentsTest(boolean success, boolean emptyTimeFilter) {
        Country country = mock(Country.class);
        when(countryService.getCountryById(anyInt())).thenReturn(country);
        when(tournamentRepository.findTournamentByCountry(any(Country.class))).thenReturn(Streamable.of(tournaments));
        when(bettingEntityFilter.filterTournaments(Streamable.of(tournaments), emptyTimeFilter ? emptyFilter : nonEmptyTimeFilter))
                .thenReturn(success ? tournaments : Collections.emptyList());
    }
}