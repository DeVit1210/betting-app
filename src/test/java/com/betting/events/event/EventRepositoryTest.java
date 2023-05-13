package com.betting.events.event;

import com.betting.events.tournament.Tournament;
import com.betting.events.tournament.TournamentRepository;
import com.betting.test_builder.impl.EventBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class EventRepositoryTest {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    private final Tournament tournament;
    private final Event futureEventCorrectTournamentTopIsTrue;
    private final Event futureEventCorrectTournamentTopIsFalse;
    private final Event futureEventWrongTournamentTopIsTrue;
    private final Event liveEventWrongTournamentTopIsFalse;
    private final String firstEventName = "Barca-Real";
    private final String secondEventName = "Barca-Atletico";
    private final String thirdEventName = "Neman-Dynamo";
    private final String fourthEventName = "MU-MC";

    EventRepositoryTest() {
        EventBuilder eventBuilder = EventBuilder.anEventBuilder();
        tournament = Tournament.builder().tournamentName("La Liga").build();
        futureEventCorrectTournamentTopIsTrue = eventBuilder
                .withName(firstEventName).withTournament(tournament).withTop(true)
                .build();
        futureEventCorrectTournamentTopIsFalse = eventBuilder
                .withName(secondEventName).withTournament(tournament)
                .build();
        futureEventWrongTournamentTopIsTrue = eventBuilder
                .withName(thirdEventName).withTop(true)
                .build();
        liveEventWrongTournamentTopIsFalse = eventBuilder
                .withName(fourthEventName).withLive(true).withStartTime(LocalDateTime.now().minusHours(1))
                .build();
    }
    @BeforeEach
    void setUp() {
        tournamentRepository.save(tournament);
    }
    @AfterEach
    void tearDown() {
        eventRepository.deleteAll();
        tournamentRepository.deleteAll();
    }
    @Test
    void testFindAllByTopIsTrueOrderByNameDesc() {
        saveAll();
        Streamable<Event> result = eventRepository.findAllByTopIsTrueOrderByNameAsc();
        assertEquals(2, result.toList().size());
        List<String> names = result.map(Event::getName).toList();
        assertEquals(names.get(0), "Barca-Real");
        assertEquals(names.get(1), "Neman-Dynamo");
    }
    private void saveAll() {
        eventRepository.saveAll(List.of(
                futureEventCorrectTournamentTopIsTrue,
                futureEventCorrectTournamentTopIsFalse,
                futureEventWrongTournamentTopIsTrue,
                liveEventWrongTournamentTopIsFalse)
        );
    }
    @Test
    void testFindAllByTopIsTrueOrderByNameDescNotFound() {
        eventRepository.save(futureEventCorrectTournamentTopIsFalse);
        Streamable<Event> result = eventRepository.findAllByTopIsTrueOrderByNameAsc();
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllByTournamentAndStartTimeIsAfterOrderByStartTimeAsc() {
        saveAll();
        Streamable<Event> result = eventRepository
                .findAllByTournamentAndStartTimeIsAfterOrderByStartTimeAsc(tournament, LocalDateTime.now());
        assertEquals(2, result.toList().size());
        List<String> names = result.map(Event::getName).toList();
        assertTrue(names.contains(firstEventName));
        assertTrue(names.contains(secondEventName));
    }
    @Test
    void testFindAllByTournamentAndStartTimeIsAfterOrderByStartTimeAscEmpty() {
        eventRepository.save(futureEventWrongTournamentTopIsTrue);
        Streamable<Event> result = eventRepository
                .findAllByTournamentAndStartTimeIsAfterOrderByStartTimeAsc(tournament, LocalDateTime.now());
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllByStartTimeAfterSuccess() {
        saveAll();
        List<Event> result = eventRepository.findAllByStartTimeAfter(LocalDateTime.now());
        assertEquals(3, result.size());
    }
}