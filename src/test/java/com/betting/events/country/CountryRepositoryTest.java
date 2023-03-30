package com.betting.events.country;

import com.betting.events.sport.Sport;
import com.betting.events.sport.SportRepository;
import com.betting.events.tournament.Tournament;
import com.betting.events.tournament.TournamentRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
@TestPropertySource("classpath:test.properties")
class CountryRepositoryTest {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private SportRepository sportRepository;
    private Sport sport;
    @BeforeEach
    void setUp() {
        sport = new Sport("Football", Collections.emptyList(), true);
        sportRepository.save(sport);
        Country belarus = new Country(1, "Belarus", sport, Collections.emptyList());
        Country russia = new Country(2, "Russia", null, Collections.emptyList());
        Country ukraine = new Country(3, "Ukraine", sport, Collections.emptyList());
        countryRepository.saveAll(List.of(belarus, russia, ukraine));
    }
    @AfterEach
    void tearDown() {
        countryRepository.deleteAll();
        sportRepository.deleteAll();
    }
    @Test
    void testFindAllCountriesBySport() {
        Streamable<Country> countries = countryRepository.findAllCountriesBySport(sport);
        assertEquals(countries.toList().size(), 2);
        List<String> countryNames = countries.map(country -> String.valueOf(country.getName())).toList();
        assertTrue(countryNames.contains("Belarus"));
        assertTrue(countryNames.contains("Ukraine"));
    }

    @Test
    void testFindAllCountriesBySportEmpty() {
        Sport anotherSport = new Sport("Hockey", Collections.emptyList(), true);
        sportRepository.save(anotherSport);
        Streamable<Country> countries = countryRepository.findAllCountriesBySport(anotherSport);
        assertTrue(countries.isEmpty());
    }
}