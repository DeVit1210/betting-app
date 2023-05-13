package com.betting.security.auth.mapping;

import com.betting.events.event.Event;
import com.betting.events.event.EventDto;

public class EventDtoMapper implements ObjectMapper<Event, EventDto> {
    @Override
    public EventDto mapFrom(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .firstOpponentName(event.getFirstOpponentName())
                .secondOpponentName(event.getSecondOpponentName())
                .time(event.getStartTime())
                .build();
    }
}