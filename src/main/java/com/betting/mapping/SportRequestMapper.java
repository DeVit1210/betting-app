package com.betting.mapping;

import com.betting.events.sport.Sport;
import com.betting.events.sport.SportRequest;
import com.betting.events.sport.SportType;
import com.betting.results.combinator.ScoreCombinatorType;
import org.springframework.stereotype.Component;

@Component
public class SportRequestMapper implements ObjectMapper<SportRequest, Sport> {
    @Override
    public Sport mapFrom(SportRequest request) {
        return Sport.builder()
                .name(request.name())
                .sportType(SportType.valueOf(request.sportType()))
                .combinatorType(ScoreCombinatorType.valueOf(request.combinatorType()))
                .top(request.top())
                .build();
    }
}
