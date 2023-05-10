package com.betting.results;

import com.betting.events.sport.SportType;
public class EventResultsFactory {
    public static EventResults createEventResults(SportType sportType) {
        return switch (sportType) {
            case FIGHT -> new FightEventResults();
            case PLAYING_SPORT -> new PlayingSportResults();
        };
    }
}
