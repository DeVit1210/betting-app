package com.betting.events.event;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.tournament.Tournament;
import com.betting.events.tournament.TournamentService;
import com.betting.events.util.BettingEntityFilter;
import com.betting.events.util.ThrowableUtils;
import com.betting.exceptions.EntityNotFoundException;
import com.betting.exceptions.ResultsAlreadySetException;
import com.betting.mapping.EventAddingRequestMapper;
import com.betting.mapping.EventDtoMapper;
import com.betting.results.EventResults;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final TournamentService tournamentService;
    private final BettingEntityFilter bettingEntityFilter;
    private final BeanFactory beanFactory;
    public BettingResponse getTopEvents() {
        Streamable<Event> foundEvents = eventRepository.findAllByTopIsTrueOrderByNameAsc();
        EventDtoMapper mapper = beanFactory.getBean(EventDtoMapper.class);
        List<EventDto> eventDtos = foundEvents.stream().map(mapper::mapFrom).toList();
        return BettingResponse.builder().entities(eventDtos).build();
    }
    public BettingResponse getAllFutureEvents() {
        List<Event> foundEvents = eventRepository.findAllByStartTimeAfter(LocalDateTime.now());
        EventDtoMapper mapper = beanFactory.getBean(EventDtoMapper.class);
        List<EventDto> eventDtos = foundEvents.stream().map(mapper::mapFrom).toList();
        return BettingResponse.builder().entities(eventDtos).build();
    }

    public BettingResponse getEventsByTournament(Long tournamentId, Integer timeFilter) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        Streamable<Event> allEvents =
                eventRepository.findAllByTournamentAndStartTimeIsAfterOrderByStartTimeAsc(tournament, LocalDateTime.now());
        List<Event> result = bettingEntityFilter.filterEvents(allEvents, timeFilter);
        EventDtoMapper mapper = beanFactory.getBean(EventDtoMapper.class);
        List<EventDto> eventDtos = result.stream().map(mapper::mapFrom).toList();
        return BettingResponse.builder().entities(eventDtos).build();
    }
    // TODO: test the following methods
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class));
    }
    public void bind(Event event, EventResults eventResults) {
        ThrowableUtils.trueOrElseThrow(e -> Objects.isNull(e.getResults()), event, ResultsAlreadySetException.class);
        eventResults.setEvent(event);
        event.setResults(eventResults);
    }
    public void updateResults(Long eventId, EventResults eventResults) {
        eventRepository.updateResults(eventId, eventResults);
    }
    public EventDto addEvent(EventAddingRequest request, Long tournamentId) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        EventAddingRequestMapper mapper = beanFactory.getBean(EventAddingRequestMapper.class, tournament);
        Event event = mapper.mapFrom(request);
        eventRepository.save(event);
        tournament.getEvents().add(event);
        return beanFactory.getBean(EventDtoMapper.class).mapFrom(event);
    }
    public Event deleteEvent(Long eventId) {
        Event event = getEventById(eventId);
        Tournament tournament = event.getTournament();
        ThrowableUtils.trueOrElseThrow(e -> Objects.isNull(e.getResults()), event, UnsupportedOperationException.class);
        tournament.getEvents().remove(event);
        eventRepository.delete(event);
        return event;
    }
}
