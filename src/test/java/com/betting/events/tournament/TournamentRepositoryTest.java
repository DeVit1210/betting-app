package com.betting.events.tournament;

import com.betting.events.country.Country;
import com.betting.events.country.CountryRepository;
import com.betting.events.sport.Sport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
class TournamentRepositoryTest {
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private CountryRepository countryRepository;
    private Country country;

    @BeforeEach
    void setUp() {
        country = Country.builder().name("Spain").tournaments(Collections.emptyList()).build();
        countryRepository.save(country);
        Tournament laLiga = Tournament.builder().tournamentName("La Liga").country(country).events(Collections.emptyList()).build();
        Tournament EPL = Tournament.builder().tournamentName("EPL").country(null).events(Collections.emptyList()).build();
        Tournament copaDelRey = Tournament.builder().tournamentName("Copa Del Rey").country(country).events(Collections.emptyList()).build();
        tournamentRepository.saveAll(List.of(laLiga, EPL, copaDelRey));
    }

    @AfterEach
    void tearDown() {
        tournamentRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    void testGetTournamentsByCountrySuccess() {
        Streamable<Tournament> tournaments = tournamentRepository.findTournamentByCountry(country);
        List<String> tournamentNames = tournaments.map(Tournament::getTournamentName).toList();
        assertEquals(2, tournaments.toList().size());
        assertTrue(tournamentNames.contains("La Liga"));
        assertTrue(tournamentNames.contains("Copa Del Rey"));
    }

    @Test
    void testGetTournamentsByCountryNotFound() {
        Country wrongCountry = Country.builder().name("Belarus").tournaments(Collections.emptyList()).build();
        countryRepository.save(wrongCountry);
        Streamable<Tournament> tournaments = tournamentRepository.findTournamentByCountry(wrongCountry);
        assertTrue(tournaments.isEmpty());
    }
}