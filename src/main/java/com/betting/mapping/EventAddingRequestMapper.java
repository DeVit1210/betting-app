package com.betting.mapping;

import com.betting.events.event.Event;
import com.betting.events.event.EventAddingRequest;
import com.betting.events.tournament.Tournament;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component("eventAddingMapper")
@Scope("prototype")
public class EventAddingRequestMapper implements ObjectMapper<EventAddingRequest, Event> {
    private final Tournament tournament;

    public EventAddingRequestMapper(Tournament tournament) {
        this.tournament = tournament;
    }

    @Override
    public Event mapFrom(EventAddingRequest request) {
        return Event.builder()
                .name(request.name())
                .tournament(tournament)
                .top(request.top())
                .firstOpponentName(request.firstOpponentName())
                .secondOpponentName(request.secondOpponentName())
                .startTime(LocalDateTime.parse(request.localDateTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                .ended(false)
                .live(false)
                .build();
    }
}
