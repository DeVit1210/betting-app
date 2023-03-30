package com.betting.events.sport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SportRepositoryTest {
    @Autowired
    private SportRepository sportRepository;
    @AfterEach
    void tearDown() {
        sportRepository.deleteAll();
    }
    @Test
    void testFindAllSportByTopIsTrueOrderByNameAsc() {
        Sport football = new Sport("Football", Collections.emptyList(), true);
        Sport hockey = new Sport("Hockey", Collections.emptyList(), true);
        Sport badminton = new Sport("Badminton", Collections.emptyList(), false);
        Sport tennis = new Sport("Tennis", Collections.emptyList(), true);
        sportRepository.saveAll(List.of(football, hockey, badminton, tennis));
        List<Sport> topSports = sportRepository.findAllSportByTopIsTrueOrderByNameAsc();
        assertEquals(topSports.size(), 3);
        List<String> topSportsNames = topSports.stream().map(sport -> String.valueOf(sport.getName())).toList();
        assertEquals(topSportsNames.get(0), "Football");
        assertEquals(topSportsNames.get(1), "Hockey");
        assertEquals(topSportsNames.get(2), "Tennis");
    }
    @Test
    void testFindAllSportByTopIsTrueOrderByNameAscEmpty() {
        sportRepository.save(new Sport("Darts", Collections.emptyList(), false));
        sportRepository.save(new Sport("WaterPolo", Collections.emptyList(), false));
        List<Sport> topSports = sportRepository.findAllSportByTopIsTrueOrderByNameAsc();
        assertTrue(topSports.isEmpty());
    }
}