package com.betting.mapping;

import com.betting.events.sport.Sport;
import com.betting.events.sport.SportDto;
import org.springframework.stereotype.Component;

@Component
public class SportDtoMapper implements ObjectMapper<Sport, SportDto> {
    @Override
    public SportDto mapFrom(Sport sport) {
        return SportDto.builder()
                .name(sport.getName())
                .top(sport.isTop())
                .eventsQuantity(sport.getEventsQuantity())
                .build();
    }
}
