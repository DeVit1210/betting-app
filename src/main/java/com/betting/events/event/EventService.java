package com.betting.events.event;

import com.betting.events.betting_entity.BettingResponse;
import com.betting.events.exception.EntityNotFoundException;
import com.betting.events.tournament.Tournament;
import com.betting.events.tournament.TournamentService;
import com.betting.events.util.BettingEntityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final TournamentService tournamentService;
    private final BettingEntityFilter bettingEntityFilter;
    public BettingResponse getTopEvents() {
        return BettingResponse.builder().entities(eventRepository.findAllByTopIsTrueOrderByNameAsc().toList()).build();
    }
    public BettingResponse getAllFutureEvents() {
        return BettingResponse.builder().entities(eventRepository.findAllByStartTimeAfter(LocalDateTime.now())).build();
    }

    public BettingResponse getEventsByTournament(Long tournamentId, Integer timeFilter) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        Streamable<Event> allEvents =
                eventRepository.findAllByTournamentAndStartTimeIsAfterOrderByStartTimeAsc(tournament, LocalDateTime.now());
        List<Event> result = bettingEntityFilter.filterEvents(allEvents, timeFilter);
        return BettingResponse.builder().entities(result).build();
    }
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class));
    }
}
