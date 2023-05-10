package com.betting.events.event;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.events.tournament.Tournament;
import com.betting.events.tournament.TournamentService;
import com.betting.events.util.BettingEntityFilter;
import com.betting.security.auth.mapping.EventDtoMapper;
import com.betting.security.auth.mapping.StakeDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Streamable;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// TODO: consider reducing of test methods with or without time filter in all the services

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class EventServiceTest {
    @InjectMocks
    private EventService eventService;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private TournamentService tournamentService;
    @Mock
    private BettingEntityFilter bettingEntityFilter;
    @Mock
    private BeanFactory beanFactory;
    @Value("${test.exception.tournament-not-found}")
    private String tournamentNotFoundMessage;
    @Value("${test.emptyTimeFilter}")
    private Integer emptyFilter;
    @Value("${test.timeFilter}")
    private Integer nonEmptyTimeFilter;
    <T> List<T> getMockList(Class<T> classType, int listSize) {
        List<T> list = new ArrayList<>();
        IntStream.range(0,listSize).forEach(value -> list.add(mock(classType)));
        return list;
    }

    @BeforeEach
    void setUp() {
        EventDtoMapper mapper = new EventDtoMapper();
        when(beanFactory.getBean(any(Class.class))).thenReturn(mapper);
    }

    @Test
    void testGetAllTopEventsSuccess() {
        List<Event> events = getMockList(Event.class, 5);
        when(eventRepository.findAllByTopIsTrueOrderByNameAsc()).thenReturn(Streamable.of(events));
        BettingResponse response = eventService.getTopEvents();
        assertNotNull(response);
    }

    @Test
    void testGetFutureEventsSuccess() {
        List<Event> events = getMockList(Event.class, 5);
        when(eventRepository.findAll()).thenReturn(events);
        BettingResponse response = eventService.getAllFutureEvents();
        assertNotNull(response.getEntities());
    }

    @Test
    void testGetEventsByTournamentWrongTournamentId() {
        when(tournamentService.getTournamentById(anyLong()))
                .thenThrow(new EntityNotFoundException(tournamentNotFoundMessage));
        assertThatThrownBy(() -> eventService.getEventsByTournament(1L, 1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(tournamentNotFoundMessage);
    }
    @Test
    void testGetEventsByTournamentSuccessEmptyFilter() {
        setUpGetEventsByTournamentTest(true);
        BettingResponse response = eventService.getEventsByTournament(1L, emptyFilter);
        assertNotNull(response.getEntities());
    }

    @Test
    void testGetEventsByTournamentNotFoundEmptyFilter() {
        setUpGetEventsByTournamentTest(false);
        BettingResponse response = eventService.getEventsByTournament(1L, emptyFilter);
        assertNotNull(response.getEntities());
    }
    @Test
    void testGetEventsByTournamentSuccessWithTimeFilter() {
        setUpGetEventsByTournamentTest(true);
        BettingResponse response = eventService.getEventsByTournament(1L, nonEmptyTimeFilter);
        assertNotNull(response.getEntities());
    }

    @Test
    void testGetEventsByTournamentNotFoundWithTimeFilter() {
        setUpGetEventsByTournamentTest(false);
        BettingResponse response = eventService.getEventsByTournament(1L, nonEmptyTimeFilter);
        assertNotNull(response.getEntities());
    }

    private void setUpGetEventsByTournamentTest(boolean success) {
        Tournament tournament = mock(Tournament.class);
        when(tournamentService.getTournamentById(anyLong())).thenReturn(tournament);
        List<Event> events = getMockList(Event.class, 5);
        when(eventRepository.findAllByTournamentAndStartTimeIsAfterOrderByStartTimeAsc(
                any(Tournament.class), any(LocalDateTime.class)
        )).thenReturn(Streamable.of(events));
        when(bettingEntityFilter.filterEvents(Streamable.of(anyList()), anyInt()))
                .thenReturn(success ? events : Collections.emptyList());
    }
}