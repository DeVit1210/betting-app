package com.betting.test_builder.impl;

import com.betting.bets.stake.Stake;
import com.betting.events.event.Event;
import com.betting.events.tournament.Tournament;
import com.betting.test_builder.TestBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(staticName = "anEventBuilder")
@AllArgsConstructor
@With
public class EventBuilder implements TestBuilder<Event> {
    private String name = "eventName";
    private String firstOpponentName = "firstOpponentName";
    private String secondOpponentName = "secondOpponentName";
    private Tournament tournament = null;
    private LocalDateTime startTime = LocalDateTime.now().plusDays(1);
    private boolean top = false;
    private boolean live = false;
    private boolean ended = false;
    private List<Stake> stakes = Collections.emptyList();

    @Override
    public Event build() {
        return Event.builder()
                .name(name)
                .startTime(startTime)
                .firstOpponentName(firstOpponentName)
                .secondOpponentName(secondOpponentName)
                .tournament(tournament)
                .top(top)
                .live(live)
                .ended(ended)
                .stakes(stakes)
                .build();
    }
}
